package shop.trqq.com.supermarket.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import shop.trqq.com.R;
import shop.trqq.com.supermarket.fragments.MarketGoCartFragment;

public class MarketGoCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_go_cart2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MarketGoCartFragment fragment = new MarketGoCartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cart","1");
        fragment.setArguments(bundle);
        transaction.add(R.id.gocart_fragment_container, fragment);
        transaction.commit();
    }
}
