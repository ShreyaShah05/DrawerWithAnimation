package com.shreya.draweranimationdemo;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private LinearLayout mLinearLayout; 
	private ListView mDrawerList;
	private ImageView mDrawerImage;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCharacterTitles;
	private TypedArray imgs;
	private int imageNumber = 0;
	int x = 35;
	int y = -35;
	ArrayAdapter<String> adapter;
	Animation animation;
	
	public static HashMap<String, String> hMapText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initText();
		
		mTitle = mDrawerTitle = getTitle();
		mCharacterTitles = getResources().getStringArray(R.array.characters_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLinearLayout = (LinearLayout) findViewById(R.id.left_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerImage = (ImageView) findViewById(R.id.drawerImage);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        
        /*View footerview = getLayoutInflater().inflate(R.layout.empty_view, null);            
        mDrawerList.setEmptyView(footerview);*/
        
        // set up the drawer's list view with items and click listener
        adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mCharacterTitles);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout.getParent().requestDisallowInterceptTouchEvent(true);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                showAnimatedImages();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            setCharacter(0);
        }
        
        imgs = getResources().obtainTypedArray(R.array.drawer_images);
        
	}
	
	public void showAnimatedImages(){
	
		mDrawerImage.setImageResource(imgs.getResourceId(imageNumber, -1));
		animation = new TranslateAnimation(x, y, x, y);
		animation.setDuration(6000);
		animation.setFillAfter(true);
		mDrawerImage.startAnimation(animation);
		
		animation.setAnimationListener(new AnimationListener() {
				
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
					
			}
				
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				System.out.println(imageNumber);
				System.out.println(imgs.length());
				// TODO Auto-generated method stub
				if(imageNumber > imgs.length())
					imageNumber = 0;
				else
					imageNumber = imageNumber+1;
				
				int r = x;
				x = y;
				y = r;
				
				showAnimatedImages();	
			}
		});
	}
	
	/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	setCharacter(position);
        }
    }

    public void setCharacter(int position){
    	Fragment fragment = new CharacterFragment();
        Bundle args = new Bundle();
        args.putInt(CharacterFragment.ARG_CHAR_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mCharacterTitles[position]);
        mDrawerLayout.closeDrawer(mLinearLayout);
    }
    
	
	/**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public class CharacterFragment extends Fragment {
        public static final String ARG_CHAR_NUMBER = "character_number";

        public CharacterFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_character, container, false);
            int i = getArguments().getInt(ARG_CHAR_NUMBER);
            String charName = getResources().getStringArray(R.array.characters_array)[i];

            TextView charTextView = (TextView) rootView.findViewById(R.id.text);
            
            if (hMapText.containsKey(charName))
            	charTextView.setText(hMapText.get(charName));
            else
            	charTextView.setText("Ooopsss! We didn't find record aboy this character");
            
            ImageView charImageView = (ImageView) rootView.findViewById(R.id.char_image);
            int imageId = getRandomImageforCharacter(charName);
            if(imageId > 0)
            	charImageView.setImageResource(imageId);
            else
            	charImageView.setImageResource(R.drawable.hogwarts);
            getActivity().setTitle(charName);
            return rootView;
        }
        
        
    }
	
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		mTitle = title;
        getActionBar().setTitle(mTitle);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	    // Pass any configuration change to the drawer toggls
	    mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLinearLayout);
        return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	
	public class Users_fancystock{
		
		String id;
		String productCode;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		
	}
	
	public void initText(){
		hMapText = new HashMap<String, String>();
		hMapText.put("Harry", "A brave, courageous wizard.");
		hMapText.put("Ron", "A wizard who can go to any extent for supporting friends.");
		hMapText.put("Hermione", "A cute and intelligent withch, holding knowledge like no other.");
		hMapText.put("Dumbledore", "An old wizard, trying to protect the world from dark lord.");
		hMapText.put("Malfoy", "A wizard who wants to have all powers and fame.");
	}
	
	public int getRandomImageforCharacter (String charName){
		String charImgArr = charName.toLowerCase().concat("_images"); 
		
		int imageArrId = getResources().getIdentifier(charImgArr, "array", getPackageName());
		
		final TypedArray imgs = getResources().obtainTypedArray(imageArrId);
		final Random rand = new Random();
		final int rndInt = rand.nextInt(imgs.length());
		final int resID = imgs.getResourceId(rndInt, 0);
		
		return resID;
	}
}
