/* PlaceDetailActivity.java
 * Project E - Eric Daniels
 * Used with Place Detail activity page to display detailed information
 *    when user clicks on Place from MapActivity page
 */

package com.android.projecte.townportal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceDetailActivity extends Activity {

    private TextView nameTextView, ratingTextView, priceTextView, 
                addressTextView, phoneNumberTextView, websiteTextView;
    
    private ImageView photoImageView;
    
    private GooglePlacesSearch gpSearch;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        gpSearch = new GooglePlacesSearch( getIntent().getExtras().getString( "gpSearchType" ), 
                getIntent().getExtras().getString( "gpSearchGeoLocation" ) );
        
        // Use custom title bar
        requestWindowFeature( Window.FEATURE_CUSTOM_TITLE );
        setContentView( R.layout.activity_place_detail );
        getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title );
        ((TextView) findViewById( R.id.title ) ).setText( R.string.returnText );

        // Set Place detail TextViews
        nameTextView = (TextView) findViewById( R.id.nameText );
        ratingTextView = (TextView) findViewById( R.id.ratingText );
        priceTextView = (TextView) findViewById( R.id.priceText );
        addressTextView = (TextView) findViewById( R.id.addressText );
        phoneNumberTextView = (TextView) findViewById( R.id.phoneNumberText );
        websiteTextView = (TextView) findViewById( R.id.websiteText );
        photoImageView = (ImageView) findViewById( R.id.photoImage );

        // Set Place detail variables
        String mName = getIntent().getExtras().getString( "name" );
        double mRating = getIntent().getExtras().getDouble( "rating" );
        int mPrice = getIntent().getExtras().getInt( "price" );
        String mAddress = getIntent().getExtras().getString( "address" );
        String mPhoneNumber = getIntent().getExtras().getString( "phonenumber" );
        String mWebsite = getIntent().getExtras().getString( "website" );
        
        // Set TextViews
        nameTextView.setText( mName );
        ratingTextView.setText( ratingToStar( (int) mRating ) );
        priceTextView.setText( priceToDollar( mPrice ) );
        addressTextView.setText( mAddress );
        phoneNumberTextView.setText( mPhoneNumber );
        websiteTextView.setClickable( true );
        websiteTextView.setMovementMethod( LinkMovementMethod.getInstance() );

        if ( mWebsite != null ) {
            websiteTextView.setText( Html.fromHtml( "<a href=" + mWebsite + ">" + mWebsite ) );
        }
        
        new PhotoTask( getIntent().getExtras().getString( "photoRef" ) ).execute();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.place_detail, menu );
        return true;
    }
    
    // Async Task to get Google Places Photo
    class PhotoTask extends AsyncTask<Void, Void, PlacePhoto> {

        private String photoReference;

        public PhotoTask( String photoRef ) {

            photoReference = photoRef;
        }

        @Override
        protected PlacePhoto doInBackground( Void... unused ) {

            return gpSearch.findPlacePhoto( photoReference );
        }

        @Override
        protected void onPostExecute( PlacePhoto thePlacePhoto ) {

            Bitmap photo = thePlacePhoto.getPhoto();
            
            // Set Photo ImageView
            if ( photo != null )
                photoImageView.setImageBitmap( photo );
        }

    }
    
    private String ratingToStar( int rating ) {
        
        String result = "";
        
        for ( int i = 0; i < rating; i++ )
            result += "\u2605";
        
        if ( !result.isEmpty() )
            for ( int i = 0; i < 5-rating; i++ )
                result += "\u2606";
        else
            result = "No Rating";
        
        return result;
    }
    
    private String priceToDollar( int price ) {
        
        String result = "";
        
        for ( int i = 0; i < price; i++ )
            result += "\u0024";
            
        return result;
    }

}
