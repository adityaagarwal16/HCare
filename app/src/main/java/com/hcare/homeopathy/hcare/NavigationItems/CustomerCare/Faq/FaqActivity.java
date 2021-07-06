package com.hcare.homeopathy.hcare.NavigationItems.CustomerCare.Faq;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;

public class FaqActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

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
                        "To consult a doctor please go to main page, select or search for a disease and explain your issue in detail."),

                /*new FaqObject("I Have a health related query, where can I ask?",
                        "To get answer for all health queries please go to main page and press 'CONSULT NOW' button and write in detail."),*/

                new FaqObject("How can I chat with a doctor?",
                        "After consultation request accepted by doctor, go to 'View your consultations' from the main page and you'll be able to chat with the doctor."),

                new FaqObject("I have sent a request but I didn't get any consultation yet?",
                        "It may take anywhere from 5 to 45 minutes to assign you a doctor, please wait for sometime and once the doctor is assigned you will receive a notification."),

                new FaqObject("How can I get treatment for multiple health issues?",
                        "You can request for the major health issue and while consulting you can explain about the other issues as well. A homeopathic doctor can treat multiple issues at a time, thus feel free to ask your doctor."),

                new FaqObject("When will my medicines be delivered?",
                        "The Medicines may take anywhere from 1 to 5 days depending on the city you live in."),

                new FaqObject("My doctor is not responding to messages, what should I do?",
                        "The doctors will do the best to allot time for each patient, they will respond to your query as soon as they are online."),

        /*        new FaqObject("Is that medicine delivered to my place?",
                        "The medicines will be delivered across indian tier1, tier2,tier3,tier4 cities"),*/

                new FaqObject("Get medicines for a health issue without consultation?",
                        "Medicines cannot be suggested without first consulting the doctor. Once you have explained your issues to the doctor, they'll give you your prescription which will be home delivered to you."),

                /*new FaqObject("I only want to buy a medicine?",
                        "Medicine can not be give without consultation,please take the consultation. To consult a doctor please go to main page, press 'CONSULT NOW' button "),*/

                new FaqObject("What benefits will I get if I purchase the medicine here?",
                        "You will get Free consultation and a follow up chat with the doctor for one month. /nFree diet chart /nNo delivery charge /nTreatment for multiple health issues with one doctor in a single medicine pack"),

                new FaqObject("Can I get the prescription directly?",
                        "No, the prescription will be given only after complete consultation. The doctor will send you the the treatment which you can order directly from the app. You can later view the prescription from 'Your orders' tab."),

                new FaqObject("Where can I see the doctor's chat after consultation request is sent?",
                        "You can click the 'View your consultations' tab on the main page to view the doctor's chat."),

                new FaqObject("Where can I see the status of my ordered medicines?",

                "Your order will be delivered within 1 to 5 days, you can track your order in the  'Your Orders' section on the main page or the navigation menu."),

                new FaqObject("I am a Homeopathic doctor, how can I register in the app?",
                        "Right now we are not hiring any doctors, please drop your resume to 'connect@hcare.co.in'. We will get in touch with you once we start the hiring process."),

        };

    }



}
