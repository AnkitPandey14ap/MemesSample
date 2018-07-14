package space.apple.three.memes.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import space.apple.three.memes.fragments.ScreenSlidePageFragment;

import static space.apple.three.memes.utils.Constants.POSITION;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private int totalMemes;

    public ScreenSlidePagerAdapter(FragmentManager fm, int totalMemes) {
        super(fm);
        this.totalMemes = totalMemes;
    }

    @Override
    public Fragment getItem(int position) {
        ScreenSlidePageFragment screenSlidePageFragment = ScreenSlidePageFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        screenSlidePageFragment.setArguments(bundle);
        return screenSlidePageFragment;
    }

    @Override
    public int getCount() {
        return totalMemes;
    }
}