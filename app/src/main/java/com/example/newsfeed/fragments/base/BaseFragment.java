package com.example.newsfeed.fragments.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.newsfeed.R;
import com.example.newsfeed.eventbus.ErrorEvent;
import com.example.newsfeed.fragments.dialogs.LoadingDialogFragment;
import com.example.newsfeed.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseFragment extends Fragment {

    protected FragmentChangeListener changeListener;
    protected LoadingDialogFragment loadingDialog;
    private boolean isShownErrorDialog;
    private int numberOfRequestedLoadingDialog;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupViews();
        bindModel();
        setupClicks();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupLoadingDialog() {
        loadingDialog = LoadingDialogFragment.newInstance();
        loadingDialog.setCancelable(false);
    }

    public void showLoadingDialog() {
        numberOfRequestedLoadingDialog++;
        if (numberOfRequestedLoadingDialog == 1) {
            setupLoadingDialog();
            loadingDialog.show(getFragmentManager(), null);
        }
    }

    public void dismissLoadingDialog() {
        if (numberOfRequestedLoadingDialog == 1) {
            loadingDialog.dismiss();
            numberOfRequestedLoadingDialog--;
        } else if (numberOfRequestedLoadingDialog > 1) {
            numberOfRequestedLoadingDialog--;
        }
    }

    public void showErrorDialog() {
        showErrorDialog(getContext().getString(R.string.default_error_message));
    }

    public void showErrorDialog(String errorMessage) {
        if (!Utils.isNetworkConnected(getContext())) {
            errorMessage = getString(R.string.no_connection_error_message);
        }

        if (!isShownErrorDialog) {
            isShownErrorDialog = true;
            if (numberOfRequestedLoadingDialog >= 1) {
                loadingDialog.dismiss();
            }
            new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        dialog.dismiss();
                        if (numberOfRequestedLoadingDialog > 0) {
                            loadingDialog.show(getFragmentManager(), null);
                        }
                        isShownErrorDialog = false;
                    }).create().show();
        }
    }

    public void setChangeListener(FragmentChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    protected abstract void setupViews();

    protected abstract void findViews(View view);

    protected abstract void bindModel();

    protected abstract void setupClicks();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ErrorEvent event) {
        showErrorDialog(event.getMsg());
    }

}

