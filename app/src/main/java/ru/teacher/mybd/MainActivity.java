package ru.teacher.mybd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String CONTACT_ID_KEY = "ContactId666";
    public static final String CONTACT_FIRST_NAME_KEY = "ContactFirstName";
    public static final String CONTACT_SECOND_NAME_KEY = "ContactSecondName";
    public static final String CONTACT_PHONE_NUMBER_KEY = "ContactPhoneNumber";

    public static final int DETAILS_ACTIVITY_REQUEST_CODE = 1;

    private ContactAdapter adapter;
    private ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.ListView);

        Database database = Database.GetInstance(this);
        if (database.isEmpty())
            AddTestData(database);

        contacts = database.selectAll();
        adapter = new ContactAdapter(this, contacts);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == DETAILS_ACTIVITY_REQUEST_CODE) {
                long id = data.getLongExtra(CONTACT_ID_KEY, Integer.MIN_VALUE);
                String firstName = data.getStringExtra(CONTACT_FIRST_NAME_KEY);
                String secondName = data.getStringExtra(CONTACT_SECOND_NAME_KEY);
                String phoneNumber = data.getStringExtra(CONTACT_PHONE_NUMBER_KEY);

                Contact contact = null;
                for (Contact c : contacts) {
                    if (c.getId() == id) {
                        contact = c;
                        break;
                    }
                }

                if (contact != null) {
                    contact.setFirstName(firstName);
                    contact.setSecondName(secondName);
                    contact.setPhoneNumber(phoneNumber);

                    adapter.notifyDataSetChanged();

                    Database database = Database.GetInstance(this);
                    database.update(contact);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = contacts.get(position);

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(CONTACT_ID_KEY, contact.getId());
        intent.putExtra(CONTACT_FIRST_NAME_KEY, contact.getFirstName());
        intent.putExtra(CONTACT_SECOND_NAME_KEY, contact.getSecondName());
        intent.putExtra(CONTACT_PHONE_NUMBER_KEY, contact.getPhoneNumber());

        startActivityForResult(intent, DETAILS_ACTIVITY_REQUEST_CODE);
    }

    private void AddTestData(Database database) {
        for (int i = 0; i < 20; i++) {
            Contact temp = new Contact(
                newString(1),
                newString(2),
                newString(3)
            );
            database.insert(temp);
        }
    }

    private static String newString(int num) {
        String temp = new String();
        switch (num) {
            case 1:
                for (int i = 0; i < 5; i++)
                    temp += (char) ('A' + (int) (Math.random() * 26));
                break;
            case 2:
                for (int i = 0; i < 5; i++)
                    temp += (char) ('a' + (int) (Math.random() * 26));
                break;
            case 3:
                temp = "+79";
                for (int i = 0; i < 9; i++)
                    temp += (char) ('0' + (int) (Math.random() * 10));
                break;
        }
        return temp;
    }

    public static class ContactAdapter extends BaseAdapter {
        private final List<Contact> data;
        private final Context context;

        public ContactAdapter(Context context, List<Contact> data) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View root = convertView;
            Contact contact = data.get(position);

            if (root == null) {
                root = View.inflate(context, R.layout.item_contact, null);
            }

            TextView fullName = root.findViewById(R.id.FullName);
            TextView phone = root.findViewById(R.id.Phone);

            fullName.setText(
                String.format("%s %s", contact.getFirstName(), contact.getSecondName()));
            phone.setText(contact.getPhoneNumber());

            return root;
        }
    }
}
