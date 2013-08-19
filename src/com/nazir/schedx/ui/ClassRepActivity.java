package com.nazir.schedx.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ClassRepActivity extends MyFragment
{

    public ClassRepActivity()
    {
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
        fragmenttransaction.replace(android.R.id.content, new ClassRepListFragment());
        fragmenttransaction.addToBackStack(null);
        fragmenttransaction.commit();
    }
}
