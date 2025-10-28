package com.mobdeve.s17.group11.smartspend.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mobdeve.s17.group11.smartspend.R;

public class UIUtils {

    public static class Appearance {

        public static void dimBehind(View subjectView, View backgroundView, float dimAmount) {
            View container = subjectView.getRootView();
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) container.getLayoutParams();
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            layoutParams.dimAmount = dimAmount;
            ((WindowManager) backgroundView.getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(container, layoutParams);
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
                if(btn0Click != null) btn0Click.onClick(btnView);
                dialog.dismiss();
            });

            btnOption1.setOnClickListener(btnView -> {
                if(btn1Click != null) btn1Click.onClick(btnView);
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

}
