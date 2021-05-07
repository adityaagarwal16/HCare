package com.hcare.homeopathy.hcare.NavigationItems.Faq;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;

public class FaqActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setTitle("Help and support");

        RecyclerView faqRecycler = findViewById(R.id.faqRecycler);
        faqRecycler.setLayoutManager(new LinearLayoutManager(this));
        faqRecycler.setAdapter(new FaqAdapter(getList(), this));
    }

    public void Back(View view) {
        onBackPressed();
    }

    FaqObject[] getList() {
        return new FaqObject[] {
                new FaqObject("How to consult a doctor?",
                        " To consult a doctor please go to main page, press 'CONSULT NOW' button "),

                new FaqObject("I Have a health related query, where can I ask?",
                        "To get answer for all health queries please go to main page and press 'CONSULT NOW' button and write in detail."),

                new FaqObject("How can I chat with a doctor?",
                        "After consultation request accepted by doctor , go to recent section by pressing 'Recent' button on bottom navigation bar , and their you can find your doctor  by pressing on doctor name it will take you to chat section."),

                new FaqObject("I have sent a request but I didn't get any consultation yet?",
                        "It may take 5 to 45mins to assign you a doctor please wait, once doctor assigned you will receive a notification"),

                new FaqObject("How can I get treatment for multiple health issues?",
                        "You can request for major health issue and while consulting you can explain about other health issue too. one homeopathic doctor can treat multiple health at a time, Feel free to ask your doctor about other health issue."),

                new FaqObject("When will my medicines be delivered?",
                        "The Medicine may take 1 to 5days according to the your city tiering."),

                new FaqObject("My doctor is not responding to messages, what should I do?",
                        "The doctor will do the best to allot time for each patient,he  reply you as soon as they come online please wait."),

                new FaqObject("Is that medicine delivered to my place?",
                        "The medicines will be delivered across indian tier1, tier2,tier3,tier4 cities"),

                new FaqObject("Suggest medicines for a health issue?",
                        "Medicine can not be suggested without consultation,please take the consutation. To consult a doctor please go to main page, press 'CONSULT NOW' button "),

                new FaqObject("I only want to buy a medicine?",
                        "Medicine can not be give without consultation,please take the consutation. To consult a doctor please go to main page, press 'CONSULT NOW' button "),

                new FaqObject("What benefits will I get if I purchase the medicine here?",
                        "*You will get Free consultation and a follow chat with doctor for 1 month /n Free diet chart /n No delivery charge /n Treatment for multiple health issues with one doctor in a single medicine pack"),

                new FaqObject("Can I get the prescription directly?",
                        "No, prescription will be given after complete consultation, and prescription only provided after purchasing medicine To consult a doctor please go to main page, press 'CONSULT NOW' button "),

                new FaqObject("Where can I see the doctor's chat after request sent?",
                        "the chat section will appear in 'Recent' tab after doctor accepting your request."),

                new FaqObject("I have ordered medicine where is my medicine?",

                "Your order will be delivered within 1 to 5 days , you can track your order in 'Order' section in main page"),

                new FaqObject("I am a Homeopathic doctor, how can i register in the app?",
                        "Right now we are not hiring any doctors, please drop you resume to 'connect@hcare.co.in'. we will get in touch with once we get openings"),

        };

    }

}
