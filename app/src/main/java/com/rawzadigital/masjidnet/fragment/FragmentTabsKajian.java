package com.rawzadigital.masjidnet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rawzadigital.masjidnet.R;
import com.rawzadigital.masjidnet.utils.Tools;

public class FragmentTabsKajian extends Fragment {

    public FragmentTabsKajian() {
    }

    public static FragmentTabsKajian newInstance() {
        FragmentTabsKajian fragment = new FragmentTabsKajian();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tabs_kajian, container, false);

/*
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_1), R.drawable.image_8);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_2), R.drawable.image_9);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_3), R.drawable.image_15);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_4), R.drawable.image_14);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_5), R.drawable.image_12);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_6), R.drawable.image_2);
        Tools.displayImageOriginal(getActivity(), (ImageView) root.findViewById(R.id.image_7), R.drawable.image_5);
*/

        return root;
    }
}