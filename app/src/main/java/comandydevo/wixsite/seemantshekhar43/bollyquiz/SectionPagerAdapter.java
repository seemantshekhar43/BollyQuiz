package comandydevo.wixsite.seemantshekhar43.bollyquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by A_Devy on 11/24/2017.
 */

class SectionPagerAdapter extends FragmentPagerAdapter{
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               Struggler struggler = new Struggler();
               return struggler;
           case 1:
              Star star = new Star();
              return star;
           case 2:
               SuperStar superStar = new SuperStar();
               return superStar;
           default:
               return null;
       }

    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "STRUGGLER";
            case 1:
                return "STAR";
            case 2:
                return "SUPERSTAR";
            default:
                return null;
        }
    }
}
