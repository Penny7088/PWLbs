package com.pw.lib.amap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.hellotalk.lib.amap.R;
import com.pw.lib.lbs.log.LoggerUtils;

import java.util.Objects;

/**
 * com.pw.lib.amap
 *
 * @author : Penny (penny@pw.com)
 * @describe : 适配 androidx fragment
 * @date : 4/2/21
 */
public class AMapSupportFragment extends Fragment {

    private static final String TAG = "AMapSupportFragment";

    private MapView mMapView;

    private OnMapLoadedListener onMapLoadedListener;

    public static AMapSupportFragment newInstance() {
        return new AMapSupportFragment();
    }

    public static AMapSupportFragment newInstance(AMapOptions aMapOptions) {
        AMapSupportFragment aMapSupportFragment = new AMapSupportFragment();
        Bundle bundle = new Bundle();
        try {
            Parcel obtain = Parcel.obtain();
            aMapOptions.writeToParcel(obtain, 0);
            bundle.putByteArray("MapOptions", obtain.marshall());
        } catch (Exception e) {
            LoggerUtils.INSTANCE.i(TAG, Objects.requireNonNull(e.getMessage()));
        }
        aMapSupportFragment.setArguments(bundle);
        return aMapSupportFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoggerUtils.INSTANCE.i(TAG, "mapView:%s" + mMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                LoggerUtils.INSTANCE.i(TAG, "onMapLoaded >> Call back...");
                if (onMapLoadedListener != null) {
                    onMapLoadedListener.onMapLoaded();
                }
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.amap_support_fragment_layout, container, false);
        mMapView = view.findViewById(R.id.amap_map_view);
        LoggerUtils.INSTANCE.i(TAG, "mapView:%s" + mMapView);
        return view;
    }


    public void setOnMapLoadedListener(OnMapLoadedListener listener) {
        LoggerUtils.INSTANCE.i(TAG, "setOnMapLoadedListener:" + mMapView);
        onMapLoadedListener = listener;
    }

    public AMap getMap() {
        if (mMapView != null) {
            return mMapView.getMap();
        }
        return null;
    }

    @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onLowMemory() {
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
        super.onLowMemory();
    }


    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

}
