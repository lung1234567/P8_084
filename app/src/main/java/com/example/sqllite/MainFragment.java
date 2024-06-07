package com.example.sqllite;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;
import android.widget.Toast;
public class MainFragment extends Fragment implements
        View.OnClickListener,RecyclerviewAdapter.OnUserClickListener{
    RecyclerView recyclerView;
    EditText edtName;
    EditText edtAge;
    Button btnSubmit;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<PersonBean> listPersonInfo;
    public MainFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getActivity();
        recyclerView=view.findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        edtName=view.findViewById(R.id.edtname);
        edtAge=view.findViewById(R.id.edtage);
        btnSubmit=view.findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(this);
        setupRecyclerView();
    }
    private void setupRecyclerView() {
        DatabaseHelper db=new DatabaseHelper(context);
        listPersonInfo=db.selectUserData();
        RecyclerviewAdapter adapter=new
                RecyclerviewAdapter(context,listPersonInfo,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }@Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnsubmit) {
            String name = edtName.getText().toString();
            String ageStr = edtAge.getText().toString();

            if (name.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(context, "Masukkan Nama dan Umur", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int age = Integer.parseInt(ageStr);

                DatabaseHelper db = new DatabaseHelper(context);
                PersonBean currentPerson = new PersonBean();
                currentPerson.setName(name);
                currentPerson.setAge(age);

                String btnStatus = btnSubmit.getText().toString();
                if (btnStatus.equals("Submit")) {
                    db.insert(currentPerson);
                } else if (btnStatus.equals("Update")) {

                }

                setupRecyclerView();
                edtName.setText("");
                edtAge.setText("");
                edtName.setFocusableInTouchMode(true);
                edtName.setFocusable(true);
                btnSubmit.setText("Submit");

            } catch (NumberFormatException e) {
                Toast.makeText(context, "Masukkan umur yang valid", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onUserClick(PersonBean currentPerson, String action) {
        if(action.equals("Edit")){
            edtName.setText(currentPerson.getName());
            edtName.setFocusable(false);
            edtAge.setText(currentPerson.getAge()+"");
            btnSubmit.setText("Update");
        }
        if(action.equals("Delete")){
            DatabaseHelper db=new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }
}