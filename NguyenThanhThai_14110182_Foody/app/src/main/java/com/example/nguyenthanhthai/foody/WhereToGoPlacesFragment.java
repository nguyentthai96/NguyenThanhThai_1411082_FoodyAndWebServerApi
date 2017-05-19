package com.example.nguyenthanhthai.foody;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenthanhthai.foody.adapter.HeaderRecyclerViewAdapter;
import com.example.nguyenthanhthai.foody.adapter.PlacesRecyclerAdapter;
import com.example.nguyenthanhthai.foody.adapter.ViewPagerAdapter;
import com.example.nguyenthanhthai.foody.listener.CategoryListener;
import com.example.nguyenthanhthai.foody.listener.DistrictStreetListener;
import com.example.nguyenthanhthai.foody.custommodel.HeaderItem;
import com.example.nguyenthanhthai.foody.customview.RecyclerViewHeader;
import com.example.nguyenthanhthai.foody.listener.LastestListener;
import com.example.nguyenthanhthai.foody.model.Category;
import com.example.nguyenthanhthai.foody.custommodel.District_;
import com.example.nguyenthanhthai.foody.model.CategoryType;
import com.example.nguyenthanhthai.foody.model.Lastest;
import com.example.nguyenthanhthai.foody.modelnew.Restaurant;
import com.example.nguyenthanhthai.foody.model.Street;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class WhereToGoPlacesFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private PlacesRecyclerAdapter mAdapter;
    private List<Restaurant> restaurants;

    private Button buttonCancel;


    // Filter id, default =0 <=> none filter
    private Long categoryTypeIdSelected = 0L;
    private Long categoryId = 0L;
    private Long districtId = 0L;
    private Long streetId = 0L;

    // Current local
    Double latitude = 10.8517968;
    Double longtitude = 106.7698624;

    private LastestFragment lastestFrag;
    private CategoryFragment categoryFrag;
    private AddressFragment addresFrag;

    private boolean loadNear = false;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    MainActivity.ListenChangeCategory changeCategoryType;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public WhereToGoPlacesFragment(MainActivity.ListenChangeCategory changeCategoryType,
                                   Long categoryTypeIdSelected) {
        this.changeCategoryType = changeCategoryType;
        this.categoryTypeIdSelected = categoryTypeIdSelected;
        this.restaurants = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        editor = sharedPreferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_where_to_go, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setHorizontalScrollBarEnabled(false);
        setupViewPager();

        buttonCancel = (Button) rootView.findViewById(R.id.cancelAction);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

//        restaurants.addAll(0, Select.from(Restaurant.class)
//                .where(Condition.prop("CATEGORYTYPEID").eq(categoryTypeIdSelected)).limit("2").list());

        //Load fist
        Restaurant.selectWithCategoryTypeLimit(this.getContext(), 1L, categoryId, districtId, streetId,
                new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
            @Override
            public void onSuccess(List<Restaurant> restaurant) {
                restaurants.addAll(restaurant);
            }
        });

        mAdapter = new PlacesRecyclerAdapter(restaurants);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) rootView.findViewById(R.id.headRecyclerView);
        recyclerViewHeader.attachToRecyclerView(recyclerView);

        RecyclerView recyclerViewChild = (RecyclerView) rootView.findViewById(R.id.recyclerViewChild);
        recyclerViewChild.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerViewChild.setItemAnimator(new DefaultItemAnimator());
        HeaderRecyclerViewAdapter mAdapterHeader = new HeaderRecyclerViewAdapter(HeaderItem.setDataHeader());
        recyclerViewChild.setAdapter(mAdapterHeader);

        addEvents();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return rootView;
    }

    /*
    * Add event for controls, update status for tab
    */
    private void addEvents() {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                viewPager.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
                if (viewPager.getVisibility() == View.VISIBLE) {
                    viewPager.setVisibility(View.GONE);
                    buttonCancel.setVisibility(View.GONE);
                } else {
                    viewPager.setVisibility(View.VISIBLE);
                    buttonCancel.setVisibility(View.VISIBLE);
                }
            }
        });

        changeCategoryType.setOnClickItem(new MainActivity.ListenChangeCategory.ChangeCategoryType() {
            @Override
            public void categoryTypeIdResult(CategoryType categoryType) {

            }

            @Override
            public void categoryTypeIdResult(Long categoryTypeId) {
//TODO
//                restaurants.addAll(0, Restaurant.find(Restaurant.class,
//                        "CATEGORYTYPEID=?", categoryTypeId.toString()));

                categoryTypeIdSelected = categoryTypeId;
                Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId, new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
                    @Override
                    public void onSuccess(List<Restaurant> restaurant) {
                        restaurants.clear();
                        restaurants.addAll(0, restaurant);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        // Envent load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if ((((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= (restaurants.size() - 1))&& dy>10) {
                    loadMore();
                }
            }
        });
    }

    /**
     * Add name tabwidget
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Lastest");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Category");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("HCM");
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    /**
     * Add three tab Lastest, Category, Address
     */
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        //Three tab
        lastestFrag = new LastestFragment(Lastest.getListLastestWhere(),
                sharedPreferences.getLong("LASTEST_SELECTED_INDEX", 0));
        adapter.addFrag(lastestFrag, "Lastest");

        categoryFrag = new CategoryFragment();
        adapter.addFrag(categoryFrag, "Category");

        addresFrag = new AddressFragment();
        adapter.addFrag(addresFrag, "HCM");
        viewPager.setAdapter(adapter);

        lastestFrag.setOnClick(new LastestListener() {
            @Override
            public void onItemClick(Lastest lastest) {

            }

            @Override
            public void onItemClick(Long lastestId) {
                viewPager.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                if (lastestId == 1) {
                    loadNear = true;
                    Restaurant.selectRetaurntNear(getContext(), latitude, longtitude, 0L, 1L, new Restaurant.VolleyCallback() {
                        @Override
                        public void onSuccess(List<Restaurant> restaurantsNew) {
                            restaurants.clear();
                            restaurants.addAll(0, restaurantsNew);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    loadNear = false;
                }
            }
        });

        categoryFrag.setDataItemTabSelect(new CategoryListener() {
            @Override
            public void onItemClick(Category category) {
                viewPager.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);

//                TODO
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "CATEGORYID=?", category.getId().toString()));

                categoryId = category.getId();

                Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId,
                        new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
                    @Override
                    public void onSuccess(List<Restaurant> restaurant) {
                        restaurants.clear();
                        restaurants.addAll(0, restaurant);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

        addresFrag.getDataTab(new DistrictStreetListener() {
            @Override
            public void onItemChildClick(Street street) {
                viewPager.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                //                TODO
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "STREETID=? and DISTRICTID=?", street.getId().toString(), street.getDistrictid().toString()));
                streetId = street.getId();

                Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId,
                        new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
                    @Override
                    public void onSuccess(List<Restaurant> restaurant) {
                        restaurants.clear();
                        restaurants.addAll(0, restaurant);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onItemParentClick(District_ district) {
                viewPager.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                //                TODO
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "DISTRICTID=?", district.getId().toString()));
                districtId = district.getId().longValue();

                Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId, new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
                    @Override
                    public void onSuccess(List<Restaurant> restaurant) {
                        restaurants.clear();
                        restaurants.addAll(0, restaurant);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }


    /*
    * Load add 1 item for filter and 2 item near
    * */
    private void loadMore() {
        if (loadNear) {
            Restaurant.selectRetaurntNear(getContext(), latitude, longtitude, new Long(restaurants.size()), new Long(restaurants.size() + 2), new Restaurant.VolleyCallback() {
                @Override
                public void onSuccess(List<Restaurant> restaurantsNew) {
                    restaurants.addAll(restaurantsNew);
                    mAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId, new Long(restaurants.size()), new Long(restaurants.size() + 1), new Restaurant.VolleyCallback() {
                @Override
                public void onSuccess(List<Restaurant> restaurant) {
                    restaurants.addAll(restaurant);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                longtitude = mLastLocation.getLongitude(); //Get local
                latitude = mLastLocation.getLatitude();
            } else {
                Toast.makeText(this.getContext(), "You are turn on GPS", Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}