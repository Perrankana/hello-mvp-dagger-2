package com.example.bradcampbell.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bradcampbell.app.hello1.DaggerHello1Component;
import com.example.bradcampbell.presentation.HelloPresenter;
import com.example.bradcampbell.presentation.HelloView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import nz.bradcampbell.compartment.PresenterControllerFragment;

import static com.example.bradcampbell.app.App.getAppComponent;

public class HelloFragment extends PresenterControllerFragment<HelloComponent, HelloPresenter> implements HelloView {
    @Inject HelloPresenter presenter;

    @InjectView(R.id.text_view) TextView textView;
    @InjectView(R.id.loading) View loadingView;

    @State boolean isLoading = false;

    @Override protected HelloComponent onCreateNonConfigurationComponent() {
        return DaggerHello1Component.builder()
                .appComponent(getAppComponent(getActivity()))
                .build();
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hello1, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        if (isLoading) {
            showLoading();
        } else {
            presenter.load();
        }
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override public void display(CharSequence stuff) {
        textView.setText(stuff);
    }

    @Override public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        isLoading = true;
    }

    @Override public void hideLoading() {
        loadingView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        isLoading = false;
    }

    @OnClick(R.id.load) public void load() {
        presenter.load();
    }

    @OnClick(R.id.clear_memory_cache) public void clearMemoryCache() {
        presenter.clearMemoryCache();
    }

    @OnClick(R.id.clear_memory_and_disk_cache) public void clearMemoryAndDiskCache() {
        presenter.clearMemoryAndDiskCache();
    }
}