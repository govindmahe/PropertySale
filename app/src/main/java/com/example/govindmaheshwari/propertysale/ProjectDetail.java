package com.example.govindmaheshwari.propertysale;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectDetail extends ActionBarActivity {
    ProgressDialog pDialog=null;
    private static String url;
    Bundle bundle=null;
    ImageLoader imageLoader = new ImageLoader(this);
    String jsonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getIntent().getExtras();
        setContentView(R.layout.activity_project_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        url ="http://54.254.240.217:8080/app-task/projects/"+bundle.getString("projectnum");
        new GetProjectsDetail().execute();
    }

    private class GetProjectsDetail extends AsyncTask<Void, Void, Void> {
        String str;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ProjectDetail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            JSONParser jsonParser = new JSONParser();
            jsonStr = jsonParser.makeServiceCall(url, JSONParser.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject =new JSONObject(jsonStr);

                    str = jsonObject.getString("specification");

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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject jsonObject= new JSONObject(jsonStr);
                String address1=jsonObject.getString("addressLine1");
                String address2=jsonObject.getString("addressLine2");
                String city1=jsonObject.getString("city");
                String desc =jsonObject.getString("description");
                TextView add1=(TextView)findViewById(R.id.address1);
                add1.setText(address1);
                TextView add2=(TextView)findViewById(R.id.address2);
                add2.setText(address2);
                TextView city=(TextView)findViewById(R.id.city);
                city.setText(city1);
                TextView descView=(TextView)findViewById(R.id.description);
                descView.setText(desc);
                JSONArray Imagelist =jsonObject.getJSONArray("documents");
                JSONObject image;
                String type;
                ImageView imgflag=null;
                String url;
                for(int i=0;i<Imagelist.length();i++)
                {
                    image = Imagelist.getJSONObject(i);
                    type =image.getString("type");
                    url=image.getString("reference");
                    Log.d("GOVIND ","for looping");
                    if(type.equals("Elevation Photo")) {
                        imgflag = (ImageView) findViewById(R.id.elevation);
                    }else if(type.equals("Location Map")){
                        imgflag = (ImageView) findViewById(R.id.locationlayout);
                    }else if(type.equals("Master Plan")){
                        imgflag = (ImageView) findViewById(R.id.layout);
                    }else if(type.equals("Floor Plan - 2D")){
                        imgflag = (ImageView) findViewById(R.id.flooeplan);
                    }
                    imageLoader.DisplayImage(url,imgflag);

                }
                String landmark =jsonObject.getString("landmark");
                TextView lmk=(TextView)findViewById(R.id.project_landmark);
                lmk.setText(landmark);
                String projectname =jsonObject.getString("listingName");
                TextView prj=(TextView)findViewById(R.id.project_name);
                prj.setText(projectname);
                TextView prjtxt=(TextView)findViewById(R.id.projectText);
                TextView lmktext=(TextView)findViewById(R.id.landmarkText);
                prjtxt.setVisibility(View.VISIBLE);
                lmktext.setVisibility(View.VISIBLE);
            }catch (JSONException e)
            {
                Log.d("Sales ","JSONException occured");
            }
        }
    }
}
