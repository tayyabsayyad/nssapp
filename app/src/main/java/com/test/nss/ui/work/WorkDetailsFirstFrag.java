package com.test.nss.ui.work;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.TestAdapter;

import java.util.ArrayList;
import java.util.List;


public class WorkDetailsFirstFrag extends Fragment {
    View root;
    ListView compHours;
    FragmentManager fm;
    List<AdapterDataWork> workListData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_work_details_first, container, false);
        workListData = firstHalfWorkData();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewWork = root.findViewById(R.id.firstWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData, requireContext());
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewWork.setAdapter(adapterWork);
        /*ArrayList<HashMap<String, String>> formList = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());

            JSONArray m_jArry = obj.getJSONArray("formules");
            HashMap<String, String> m_li;
            m_li = new HashMap<>();
            ArrayList<String> m = new ArrayList<>();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String formula_value = jo_inside.getString("formule");
                String url_value = jo_inside.getString("url");
                String shit_val = jo_inside.getString("shit");
                String x = jo_inside.getString("chair");
                //Log.d("Details-->", formula_value);
                //Log.d("Details-->", url_value);
                m_li.put("formule", formula_value);
                m_li.put("url", url_value);
                m_li.put("shit", shit_val);
                m_li.put("chair", x);

                formList.add(m_li);
                //Log.d("Form-->", Objects.requireNonNull(formList.get(i).get("formule")));
                Log.d("Form-->", Objects.requireNonNull(formList.get(i).get("url")));
                Log.d("Form-->", Objects.requireNonNull(formList.get(i).get("shit")));
                Log.d("Form-->", Objects.requireNonNull(formList.get(i).get("chair")));
                //Log.d("FormList-->", Objects.requireNonNull(formList.get(i).get("formule")));

                m.add(formList.get(i).get("url"));
                //ListAdapter adapter = new SimpleAdapter(requireContext(), as.get(i).get("url"), android.R.layout.simple_list_item_1,new String[]{"url"}, new int[]{R.id.url});
                ArrayAdapter adapter = new ArrayAdapter(requireContext(), R.layout.simple_list_item_1, m);
                compHours.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = requireActivity().getAssets().open("yourfilename.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fm = requireActivity().getSupportFragmentManager();
        fm.popBackStack("WorkFrag", 0);
    }

    public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        TestAdapter m = new TestAdapter(requireContext());
        m.createDatabase();
        m.open();
        int areaComp = m.getSumHours("First Year Area Based");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

        String areaRemHours;
        String univRemHours;
        String clgRemHours;

        if (areaComp>1 && 80-areaComp>0)
            areaRemHours=String.valueOf(80-areaComp);
        else
            areaRemHours = "00";

        if(clgComp>1 && 20-clgComp>0)
            clgRemHours=String.valueOf(20-clgComp);
        else
            clgRemHours="00";

        if (univComp>0 && 20-univComp>0)
            univRemHours=String.valueOf(20-univComp);
        else
            univRemHours="00";

        Log.e("AAA", ""+areaComp);
        m.close();


        data.add(new AdapterDataWork(getString(R.string.area), "80", String.valueOf(areaComp), areaRemHours));
        data.add(new AdapterDataWork(getString(R.string.univ), "20", String.valueOf(univComp), univRemHours));
        data.add(new AdapterDataWork(getString(R.string.clg), "20", String.valueOf(clgComp), clgRemHours));
        return data;
    }
}