package com.example.foodies.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodies.R;
import com.example.foodies.FoodiesSliderAdapter;
import com.example.foodies.SliderItem;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    SliderView sliderView;
    private FoodiesSliderAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sliderView = root.findViewById(R.id.imageSlider);

        adapter = new FoodiesSliderAdapter(root.getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);


        List<SliderItem> sliderItemList = new ArrayList<>();

        SliderItem itemOne = new SliderItem();

        itemOne.setImageUrl("https://i.imgur.com/16XULDC.jpg");

        itemOne.setDescription("Dumpling Haus");

        sliderItemList.add(itemOne);

        SliderItem itemTwo = new SliderItem();
        itemTwo.setImageUrl("https://i.imgur.com/1f1jRKE.jpg");
        itemTwo.setDescription("BelAir Cantina");

        sliderItemList.add(itemTwo);

        SliderItem itemThree = new SliderItem();
        itemThree.setImageUrl("https://i.imgur.com/jkinxAz.jpg");
        itemThree.setDescription("Dotty Dumpling's Dowry");

        sliderItemList.add(itemThree);

        SliderItem itemFour = new SliderItem();
        itemFour.setImageUrl("https://i.imgur.com/5eohF2F.jpg");
        itemFour.setDescription("Monty's Blue Plate Diner");

        sliderItemList.add(itemFour);

        SliderItem itemFive = new SliderItem();
        itemFive.setImageUrl("https://i.imgur.com/eXtdutL.jpg");
        itemFive.setDescription("Tornado Steakhouse");

        sliderItemList.add(itemFive);

        SliderItem itemSix = new SliderItem();
        itemSix.setImageUrl("https://i.imgur.com/W2PQrRD.jpg");
        itemSix.setDescription("BelAir Cantina");

        sliderItemList.add(itemSix);

        adapter.renewItems(sliderItemList);

        return root;
    }


//
//
//    public void renewItems(View view) {
//        List<SliderItem> sliderItemList = new ArrayList<>();
//        //dummy data
//        for (int i = 0; i < 5; i++) {
//            SliderItem sliderItem = new SliderItem();
//            sliderItem.setDescription("Slider Item " + i);
//            if (i % 2 == 0) {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//            } else {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
//            }
//            sliderItemList.add(sliderItem);
//        }
//        adapter.renewItems(sliderItemList);
//    }
//
//    public void removeLastItem(View view) {
//        if (adapter.getCount() - 1 >= 0)
//            adapter.deleteItem(adapter.getCount() - 1);
//    }
//
//    public void addNewItem(View view) {
//        SliderItem sliderItem = new SliderItem();
//        sliderItem.setDescription("Slider Item Added Manually");
//        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//        adapter.addItem(sliderItem);
//    }
//
//


}