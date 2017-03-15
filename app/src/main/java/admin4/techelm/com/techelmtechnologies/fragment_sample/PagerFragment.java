/***
 Copyright (c) 2012 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 Covered in detail in the book _The Busy Coder's Guide to Android Development_
 https://commonsware.com/Android
 */

package admin4.techelm.com.techelmtechnologies.fragment_sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import admin4.techelm.com.techelmtechnologies.R;

public class PagerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.sent_layout, container, false);
        ViewPager pager = (ViewPager) result.findViewById(R.id.pager);

        pager.setAdapter(buildAdapter());

        return (result);
    }

    private PagerAdapter buildAdapter() {
        return (new MyAdapter(getActivity(), getChildFragmentManager()));
    }

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = {
                getString(R.string.title_activity_add_replacement_part),
                getString(R.string.title_activity_part_replacement),
                getString(R.string.title_activity_service_report)};
        private Context mContext = null;

        public MyAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            this.mContext = ctx;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return SentFragment_OLD.newInstance(position);
                }
                case 1: {
                    return SentFragment_OLD.newInstance(position);
                }
                case 2: {
                    return SentFragment_OLD.newInstance(position);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
            // return(EditorFragment.getTitle(ctxt, position));
        }
    }
}