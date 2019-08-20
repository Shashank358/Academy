package com.iiysoftware.academy.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iiysoftware.academy.Fragments.AllreadyAssignStudFrag;
import com.iiysoftware.academy.Fragments.AssignStudFrag;

public class StudentAssignPager extends FragmentStatePagerAdapter {

    public StudentAssignPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AssignStudFrag();
            case 1:
                return new AllreadyAssignStudFrag();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "All Students";
            case 1:
                return "Assigned Students";
                default:
                    return super.getPageTitle(position);
        }
    }
}
