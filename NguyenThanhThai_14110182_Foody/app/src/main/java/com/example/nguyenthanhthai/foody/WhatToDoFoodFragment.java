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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.nguyenthanhthai.foody.adapter.FoodRecyclerAdapter;
import com.example.nguyenthanhthai.foody.adapter.HeaderRecyclerViewAdapter;
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

public class WhatToDoFoodFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Restaurant> restaurants;
    private RecyclerView recyclerView;
    private FoodRecyclerAdapter mAdapter;
    Button buttonCancel;


    // Filter id, default =0 <=> none filter
    private Long categoryTypeIdSelected = 0L;
    private Long categoryId = 0L;
    private Long districtId = 0L;
    private Long streetId = 0L;

    // Current local
    Double latitude = 10.8517968;
    Double longtitude = 106.7698624;

    //Google Location
    private boolean loadNear = false;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    private LastestFragment lastestFrag;
    private CategoryFragment categoryFrag;
    private AddressFragment addresFrag;

    MainActivity.ListenChangeCategory changeCategoryType;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public WhatToDoFoodFragment(MainActivity.ListenChangeCategory changeCategoryType,
                                Long categoryTypeIdSelected) {
        this.changeCategoryType = changeCategoryType;
        this.categoryTypeIdSelected = categoryTypeIdSelected;
        restaurants = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        editor = sharedPreferences.edit();

        View rootView = inflater.inflate(R.layout.fragment_what_to_do, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setHorizontalScrollBarEnabled(false);
        setupViewPager();

        buttonCancel = (Button) rootView.findViewById(R.id.cancelAction);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        Log.d("NTT", "onTab.._Food below");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        restaurants.clear();

//        restaurants.addAll(0, Select.from(Restaurant.class)
//                .where(Condition.prop("CATEGORYTYPEID").eq(categoryTypeIdSelected)).limit("8").list());

        //Load fist
        com.example.nguyenthanhthai.foody.modelnew.Restaurant.selectWithCategoryTypeLimit(this.getContext(), 1L, categoryId, districtId, streetId,
                new Long(restaurants.size()), new Long(restaurants.size() + 1), new com.example.nguyenthanhthai.foody.modelnew.Restaurant.VolleyCallback() {
                    @Override
                    public void onSuccess(List<Restaurant> restaurant) {
                        restaurants.addAll(restaurant);
                    }
                });

        mAdapter = new FoodRecyclerAdapter(restaurants);
        recyclerView.setAdapter(mAdapter);

        RecyclerViewHeader recyclerViewHeader = (RecyclerViewHeader) rootView.findViewById(R.id.headRecyclerView);
        recyclerViewHeader.attachToRecyclerView(recyclerView);

        ViewFlipper viewFlipper = (ViewFlipper) rootView.findViewById(R.id.viewflipper);

        RecyclerView recyclerViewChild = (RecyclerView) rootView.findViewById(R.id.recyclerViewChild);
        recyclerViewChild.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerViewChild.setItemAnimator(new DefaultItemAnimator());
        HeaderRecyclerViewAdapter mAdapterHeader = new HeaderRecyclerViewAdapter(HeaderItem.setDataHeader());
        recyclerViewChild.setAdapter(mAdapterHeader);

        addEvents();

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
                Log.d("NTT", "onTabSelected_Food");
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
                Log.d("NTT", "onTabReselected_Food");
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
//                restaurants.clear();
//                restaurants.addAll(0, Restaurant.find(Restaurant.class,
//                        "CATEGORYTYPEID=?", categoryTypeId.toString()));
//                mAdapter.notifyDataSetChanged();

                // Load filter CategoryType
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

                if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= restaurants.size() - 2) {
//                    restaurants.addAll(
//                            Restaurant.findWithQuery(Restaurant.class, "Select * from Restaurant where CATEGORYTYPEID = ? LIMIT 2 OFFSET ?", categoryTypeIdSelected.toString(), (restaurants.size() + 1)+""));
//                restaurants.addAll(Select.from(Restaurant.class)
//                        .where(Condition.prop("CATEGORYTYPEID").eq(categoryTypeIdSelected),Condition.prop("ID > "+restaurants.get(restaurants.size()-1).getId().toString())).limit("2").list());

                    if ((((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() >= (restaurants.size() - 1))&& dy>10) {
                        loadMore();
                    }
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

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

//                restaurants.clear();
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "CATEGORYID=?", category.getId().toString()).subList(0,4));
//                mAdapter.notifyDataSetChanged();

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

//                restaurants.clear();
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "STREETID=? and DISTRICTID=?", street.getId().toString(), street.getDistrictid().toString()));
//                mAdapter.notifyDataSetChanged();

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

//                restaurants.clear();
//                restaurants.addAll(Restaurant.find(Restaurant.class,
//                        "DISTRICTID=?", district.getId().toString()).subList(0,6));
//                mAdapter.notifyDataSetChanged();

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
    * Load add 2 item
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
            Restaurant.selectWithCategoryTypeLimit(getContext(), categoryTypeIdSelected, categoryId, districtId, streetId, new Long(restaurants.size()), new Long(restaurants.size() + 2), new Restaurant.VolleyCallback() {
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