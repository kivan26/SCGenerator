package com.example.milan.scgenerator.generators;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.milan.scgenerator.services.GenerateService;

import java.util.ArrayList;

/**
 * Created by milan on 18.11.15..
 */
public class ContactGenerator extends SmsGenerator {
    private static final String TAG = "Contact Generator";

    public ContactGenerator(Context context, GenerateService service) {
        super(context, service);
    }


    @Override
    public void generate(int number) {
        contacts(number);
    }

    /**
     * Generate values for contacts and write them
     * @param number int */
    public void contacts(int number) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        for (int i = 0; i < number; i++) {

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

            // first name , last name
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                        , super.message())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME
                        , super.message())
                .build());


            // number
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER
                        , super.random.nextInt(20000 - 1000) + 1000)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE
                        , ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
                .build());

            // email
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, mail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE
                        , ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

            try {
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                ops.clear();
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(TAG, e.getMessage());
                if (service != null) {
                    service.failed();
                }
            }

        }

        if (service != null) {
            service.finish();
        }

    }



    /**
     * Generate mail. */
    public String mail() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                stringBuilder.append("@");

            } else {
                stringBuilder.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
            }
        }

        return stringBuilder.toString();
    }




}
