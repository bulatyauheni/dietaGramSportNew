package bulat.diet.helper_sport.db;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import bulat.diet.helper_sport.utils.SaveUtils;

/**
 * Created by Yauheni.Bulat on 17.07.2017.
 */

public class CoinManager {

    public static int getCoins (Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(SaveUtils.getLang(context)));
        Date nowDate = new Date();
        int coins = SaveUtils.getCoinsCount(context);
        long curentDateandTime = SaveUtils.getCoinRefreshDate(context);

        //check previous days
        if (curentDateandTime != 0 && nowDate.getTime() - curentDateandTime > DateUtils.DAY_IN_MILLIS) {
            while (nowDate.getTime() - curentDateandTime >= DateUtils.DAY_IN_MILLIS) {
                if (TodayDishHelper.getArrayDishesByDate(context, sdf.format(curentDateandTime)).size() > 1) {
                    //coins ++;
                }else {
                    if (coins > 0) {
                        coins--;
                    }
                }
                curentDateandTime = (new Date(curentDateandTime + DateUtils.DAY_IN_MILLIS)).getTime();
            }
        }
        //check today
        if (TodayDishHelper.getArrayDishesByDate(context, sdf.format(nowDate)).size() > 1 && !sdf.format(SaveUtils.getCoinRefreshDate(context)).equals(sdf.format(nowDate))) {
            coins++;
            curentDateandTime = nowDate.getTime();
        }
        SaveUtils.setCoinRefreshDate(context, curentDateandTime);
        SaveUtils.setCoinsCount(context, coins);
        return coins;
    }

}
