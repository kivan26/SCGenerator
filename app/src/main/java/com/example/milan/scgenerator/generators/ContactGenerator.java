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
        ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();

        for (int i = 0; i < number; i++) {

            contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

            // First name and last name
            contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                        , super.message())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME
                        , super.message())
                .build());


            // Phone Number
            contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER
                        , super.random.nextInt(200000 - 10000) + 10000)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE
                        , ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
                .build());

            // Email
            contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, mail())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE
                        , ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());

            // Sip address
            contentProviderOperations.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE
                            , ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.SipAddress.DATA
                            , "+" + String.valueOf(random.nextInt(999 - 100) + 100))
                    .withValue(ContactsContract.CommonDataKinds.SipAddress.TYPE
                            , ContactsContract.CommonDataKinds.SipAddress.TYPE_CUSTOM)
                    .build());


            // Postal Structure
            contentProviderOperations.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE
                        , ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA, super.message())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE
                        , ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM)
                .build());

            // Nickname
            contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Nickname.DATA, "Mr. " + super.message())
                .withValue(ContactsContract.CommonDataKinds.Nickname.TYPE, ContactsContract.CommonDataKinds.Nickname.TYPE_CUSTOM)
                .build());

            try {
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY
                        , contentProviderOperations);
                contentProviderOperations.clear();
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(TAG, e.getMessage());
                if (service != null) {
                    service.failed();
                }
            }

        }
        // send finish status
        if (service != null) {
            service.finish();
        }

    }



    /**
     * Generate mail. */
    public String mail() {
        StringBuilder stringBuilder = new StringBuilder();

        int size = random.nextInt(20 - 10) + 10;
        int halfofsize = (size / 2);
        for (int i = 0; i < size; i++) {
            if (i == halfofsize) {
                stringBuilder.append("@");

            } else {
                stringBuilder.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
            }
        }

        return stringBuilder.toString();
    }




}
