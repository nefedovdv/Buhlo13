package grover.Buhlo13;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by ndv on 29.11.2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return MainFragment.newInstance(position);
            case 1:
                return FragmentListView.newInstance(position);
//            case 2:
//                return FragmentTwoListView.newInstance(position);

        }
        return null;
    }

//        @Override
//        public void setPrimaryItem(View container, int position, Object object) {
//            if (position==1){
//
//            }
//            super.setPrimaryItem(container, position, object);
//        }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 0";
            case 1:
                return "SECTION 1";
//            case 2:
//                return "SECTION 2";

        }
        return null;
    }

}
