/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.fragment.ReviewDetailFragment;
import com.calgen.prodek.fadflicks.fragment.ReviewFragment;
import com.calgen.prodek.fadflicks.model.Review;
import com.calgen.prodek.fadflicks.model.ReviewResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Handle setup of {@link ReviewFragment} and {@link ReviewDetailFragment}
 */
public class ReviewActivity extends AppCompatActivity implements ReviewFragment.Callback {
    //@formatter:off
    @BindView(R.id.toolbar) public Toolbar toolbar;
    //@formatter:on

    public String movieTitle;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        ReviewResponse reviewResponse = (ReviewResponse) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        movieTitle = getIntent().getStringExtra(getString(R.string.movie_title_key));

        toolbar.setTitle(movieTitle);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (MainActivity.twoPane && savedInstanceState==null) {
            ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Intent.EXTRA_TEXT, reviewResponse.getReviewResponses().get(0));
            reviewDetailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.review_detail_container, reviewDetailFragment).commit();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onItemSelected(Review review) {
        if (MainActivity.twoPane) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Intent.EXTRA_TEXT, review);

            ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
            reviewDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.review_detail_container, reviewDetailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, ReviewDetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, review);
            intent.putExtra(getString(R.string.movie_title_key), movieTitle);
            startActivity(intent);
        }
    }
}
