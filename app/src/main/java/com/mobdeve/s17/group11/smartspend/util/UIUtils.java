package com.mobdeve.s17.group11.smartspend.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesCategory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UIUtils {

    public static class Action {

        public static void unfocusAndHideKeyboard(EditText editText) {
            Context context = editText.getContext();
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);

            editText.post(() -> {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
            });
        }

    }

    public static class Appearance {

        public static void dimBehind(View subjectView, View backgroundView, float dimAmount) {
            View container = subjectView.getRootView();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = dimAmount;
            ((WindowManager) backgroundView.getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(container, layoutParams);
        }

    }

    public static class CompositeInstantiator {

        public static void categoryDropdown(DropdownComposite dropdownComposite, EditText editText) {
            dropdownComposite.listAdapter = new DropdownListAdapter();
            dropdownComposite.popupWindow = new AtomicReference<>();

            AtomicReference<PopupWindow> dropdownPopupWindow = dropdownComposite.popupWindow;
            DropdownListAdapter dropdownListAdapter = dropdownComposite.listAdapter;
            List<String> dropdownItems = dropdownComposite.items;

            dropdownListAdapter.items.addAll(dropdownItems);

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> filtered = Algorithm.filterStringSearch(dropdownItems, s.toString());

                    dropdownListAdapter.items.clear();

                    if(!filtered.isEmpty())
                        dropdownListAdapter.items.addAll(filtered);
                    else
                        dropdownListAdapter.items.add(ExpensesCategory.OTHERS.getDisplayName());

                    dropdownListAdapter.notifyDataSetChanged();

                    if(dropdownPopupWindow.get() == null) {
                        dropdownPopupWindow.set(UIUtils.Popup.showPopupDropdownList(editText, dropdownListAdapter));
                    } else if(!dropdownPopupWindow.get().isShowing()) {
                        if(dropdownListAdapter.getItemCount() != 0)
                            dropdownPopupWindow.get().showAsDropDown(editText);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {}

            });

            editText.setOnClickListener(view -> {
                if(dropdownPopupWindow.get() == null) {
                    dropdownPopupWindow.set(UIUtils.Popup.showPopupDropdownList(view, dropdownListAdapter));
                } else if(!dropdownPopupWindow.get().isShowing()) {
                    if(dropdownListAdapter.getItemCount() != 0)
                        dropdownPopupWindow.get().showAsDropDown(view);
                }
            });

            editText.setOnFocusChangeListener((view, hasFocus) -> {
                editText.setCursorVisible(hasFocus);
            });

            dropdownListAdapter.itemClick = view -> {
                String category = ((TextView) view.findViewById(R.id.tv_label)).getText().toString();

                editText.setText(Algorithm.expenseCategoryDisplayNameMap.get(category.toLowerCase()));
                editText.setSelection(category.length());

                dropdownPopupWindow.get().dismiss();
                UIUtils.Action.unfocusAndHideKeyboard(editText);
            };

        }

    }

    public static class Dialog {

        public static void showPrompt0(View anchorView, View dialogView, String header, String message, String btn0Label, String btn1Label, View.OnClickListener btn0Click, View.OnClickListener btn1Click) {
            if(dialogView == null)
                dialogView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.dialog_prompt0, null, false);

            TextView tvHeader = dialogView.findViewById(R.id.tv_header);
            TextView tvMessage = dialogView.findViewById(R.id.tv_message);
            Button btnOption0 = dialogView.findViewById(R.id.btn_option0);
            Button btnOption1 = dialogView.findViewById(R.id.btn_option1);

            tvHeader.setText(header);
            tvMessage.setText(message);
            btnOption0.setText(btn0Label);
            btnOption1.setText(btn1Label);

            android.app.Dialog dialog = new android.app.Dialog(anchorView.getContext());
            dialog.setContentView(dialogView);
            dialog.setCanceledOnTouchOutside(false);

            Window window = dialog.getWindow();

            assert window != null;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.1f);
            window.setGravity(Gravity.CENTER);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(0);

            btnOption0.setOnClickListener(btnView -> {
                if(btn0Click != null)
                    btn0Click.onClick(btnView);

                dialog.dismiss();
            });

            btnOption1.setOnClickListener(btnView -> {
                if(btn1Click != null)
                    btn1Click.onClick(btnView);

                dialog.dismiss();
            });

            dialog.show();
        }

        public static void showPrompt0(View anchorView, View dialogView, String header, String message, String btn0Label, String btn1Label, int btn0Bg, int btn1Bg, View.OnClickListener btn0Click, View.OnClickListener btn1Click) {
            if(dialogView == null)
                dialogView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.dialog_prompt0, null, false);

            Button btnOption0 = dialogView.findViewById(R.id.btn_option0);
            Button btnOption1 = dialogView.findViewById(R.id.btn_option1);

            btnOption0.setBackgroundColor(btn0Bg);
            btnOption1.setBackgroundColor(btn1Bg);

            showPrompt0(anchorView, dialogView, header, message, btn0Label, btn1Label, btn0Click, btn1Click);
        }

    }

    public static class Popup {

        public static PopupWindow showPopupDropdownList(View anchorView, DropdownListAdapter dropdownListAdapter) {
            if(dropdownListAdapter.getItemCount() == 0)
                return null;

            View view = ((Activity) anchorView.getContext()).getLayoutInflater().inflate(R.layout.popup_dropdown_list, null);
            RecyclerView recyclerView = view.findViewById(R.id.rv_list);

            recyclerView.setLayoutManager(new LinearLayoutManager(
                    anchorView.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            ));

            recyclerView.setAdapter(dropdownListAdapter);

            PopupWindow popupWindow = new PopupWindow(view, anchorView.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setAnimationStyle(0);
            popupWindow.setFocusable(false);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.showAsDropDown(anchorView);

            return popupWindow;
        }

    }

}
