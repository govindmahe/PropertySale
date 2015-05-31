package com.example.govindmaheshwari.propertysale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements GoogleMap.OnInfoWindowClickListener {
    final ArrayList<String> lists = new ArrayList<String>();
    private GoogleMap mMap;
    Map<Marker, String> map = new HashMap<Marker,String>();
    ProgressDialog pDialog=null;
    private static String url = "http://54.254.240.217:8080/app-task/projects/";
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetProjects().execute();
    }
    private void initilizeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
            if(mMap!=null)
            {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(28.6538,77.2290)).zoom(3).build();// default set delhi as a camera position

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private class GetProjects extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            JSONParser jsonParser = new JSONParser();
            String jsonStr = jsonParser.makeServiceCall(url, JSONParser.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    jsonArray =new JSONArray(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                mMap.setOnInfoWindowClickListener(MainActivity.this);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    String id = c.getString("id");
                    String name = c.getString("projectName");
                    Double lat=c.getDouble("lat");
                    Double lon=c.getDouble("lon");
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lon)).zoom(10).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    Marker mrk = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    map.put(mrk,id);
                    lists.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProjectListAdapter a = new ProjectListAdapter(getApplication(), lists);
            ListView lv = (ListView) findViewById(R.id.list);
            lv.setAdapter(a);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                String projectid;
                Double lon;
                String name;
                Double lat;
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                       JSONObject selcectedObj=  jsonArray.getJSONObject(position);
                        projectid= selcectedObj.getString("id");
                        lat=selcectedObj.getDouble("lat");
                        name = selcectedObj.getString("projectName");
                        lon=selcectedObj.getDouble("lon");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    launchDetail(projectid);
                }
            });
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String Id = map.get(marker);
        launchDetail(Id);
    }

    public void launchDetail(String id){
        Intent intent = new Intent(getApplicationContext(), ProjectDetail.class);
        intent.putExtra("projectnum", id);
        startActivity(intent);


    }
}
