package com.example.conexionrealtimedatabase;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

/** Shared visual system: high contrast, generous touch targets and clear hierarchy. */
final class Ui {
    static final int INK=Color.rgb(25,48,66), MUTED=Color.rgb(79,101,117), TEAL=Color.rgb(0,105,98), BG=Color.rgb(242,247,247), WHITE=Color.WHITE, LINE=Color.rgb(211,225,225);
    private Ui(){}
    static LinearLayout page(Activity a){LinearLayout l=new LinearLayout(a);l.setOrientation(1);l.setPadding(dp(a,20),dp(a,18),dp(a,20),dp(a,20));l.setBackgroundColor(BG);return l;}
    static TextView text(Activity a,String s,int size,int color,boolean bold){TextView t=new TextView(a);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER_VERTICAL);t.setPadding(dp(a,2),dp(a,7),dp(a,2),dp(a,7));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);return t;}
    static TextView heading(Activity a,String s){TextView t=text(a,s,26,INK,true);t.setPadding(0,dp(a,4),0,dp(a,4));return t;}
    static LinearLayout card(Activity a){LinearLayout c=new LinearLayout(a);c.setOrientation(1);c.setPadding(dp(a,16),dp(a,12),dp(a,16),dp(a,12));GradientDrawable g=new GradientDrawable();g.setColor(WHITE);g.setCornerRadius(dp(a,16));g.setStroke(dp(a,1),LINE);c.setBackground(g);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,dp(a,8),0,dp(a,8));c.setLayoutParams(p);return c;}
    static Button button(Activity a,String s,boolean primary){Button b=new Button(a);b.setText(s);b.setTextSize(16);b.setAllCaps(false);b.setMinHeight(dp(a,52));b.setPadding(dp(a,14),dp(a,8),dp(a,14),dp(a,8));b.setTextColor(primary?WHITE:TEAL);GradientDrawable g=new GradientDrawable();g.setColor(primary?TEAL:Color.rgb(225,241,239));g.setCornerRadius(dp(a,14));b.setBackground(g);return b;}
    static void showWithBack(Activity activity, LinearLayout content, String destination) {
        LinearLayout screen = new LinearLayout(activity);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setBackgroundColor(BG);
        Button back = button(activity, "Volver a " + destination, false);
        back.setOnClickListener(v -> activity.onBackPressed());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.setMargins(dp(activity, 20), dp(activity, 8), dp(activity, 20), 0);
        screen.addView(back, params);
        android.widget.ScrollView scroll = new android.widget.ScrollView(activity);
        scroll.setFillViewport(true);
        scroll.addView(content);
        screen.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        activity.setContentView(screen);
    }

    static void confirmDiscard(Activity activity) {
        new android.app.AlertDialog.Builder(activity)
            .setTitle("¿Salir sin guardar?")
            .setMessage("Se perderán los cambios de este formulario.")
            .setNegativeButton("Seguir editando", null)
            .setPositiveButton("Descartar cambios", (dialog, which) -> activity.finish())
            .show();
    }

    static int dp(Activity a,int n){return (int)(n*a.getResources().getDisplayMetrics().density+0.5f);}
}
