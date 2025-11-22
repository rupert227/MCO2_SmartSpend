package com.mobdeve.s17.group11.smartspend.util;

import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DropdownComposite {

    public AtomicReference<PopupWindow> popupWindow;
    public DropdownListAdapter listAdapter;
    public List<String> items = new ArrayList<>();

}
