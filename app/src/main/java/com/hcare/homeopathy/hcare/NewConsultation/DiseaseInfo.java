package com.hcare.homeopathy.hcare.NewConsultation;

import com.hcare.homeopathy.hcare.R;

public class DiseaseInfo {

    Diseases disease;

    public DiseaseInfo(Diseases disease) {
        this.disease = disease;
    }

    public String getDiseaseName() {
        switch(disease) {
            case thyroid:
                return "Thyroid";
            case diabetes:
                return "Diabetes";
            case skin:
                return "Skin";
            case female:
                return "Female Issues";
            case renalProblems:
                return "Renal Problems";
            case weightLossAndGain:
                return "Weight loss & gain";
            case headache:
                return "Headache";
            case hair:
                return "Hair";
            case men:
                return "Male Issues";
            case piles:
                return "Piles";
            case respiratoryProblems:
                return "Respiratory problems";
            case bonesAndJoints:
                return "Bones and joints";
            case depression:
                return "Depression";
            case growth:
                return "Growth";
            case heartProblems:
                return "Heart problems";
            case ENT:
                return "ENT";
            case mouthAndTeeth:
                return "Mouth and teeth";
            case children:
                return "Children";
            case nutritionAndHealth:
                return "Nutrition & health";
            case maternal:
                return "Maternal";
            case others:
                return "Others";

            default:
                return "";
        }
    }

    public String getInfo() {
        switch(disease) {
            case thyroid:
                return "Thyroid disease is a medical condition that affects the function of the thyroid gland. The thyroid gland is located at the front of the neck and produces thyroid hormones that travel through the blood to help regulate many other organs, meaning that it is an endocrine organ.";
            case diabetes:
                return "Diabetes is a metabolic disorder characterized by high blood sugar levels. " +
                        "Blood sugar is maintained by the pancreas when they produce sufficient amounts of insulin. " + "There are multiple associated symptoms such as increased appetite, thirst, and frequency of urine.";
            case skin:
                return "It's the largest organ of the human body." +
                        "The skin is one of the most common sites where a disease is expressed initially." +
                        "Some of the common diseases associated with the skin are are Eczema, Psoriasis, Herpes and Allergic reactions.";
            case female:
                return "A majority of females suffer from hormonal imbalances resulting in menstrual irregularities. Common diseases include Polycystic ovary syndrome , Thyroid hormone imbalance, fibroid uterus etc. Most of these cases result in mood swings which affect relationships and cause difficulties in conceiving.";
            case renalProblems:
                return "The most neglected health related problem as most of them get Palliated by drinking sufficient amounts of water. Usual diseases are urinary tract infection, renal calculi stones etc. which are expressed usually by burning or bloody urine and pain. It can lead to life threatening diseases like nephritic/nephrotic syndrome, Renal failure.";
            case weightLossAndGain:
                return "Obesity is usually caused due to a sedentary life style, habits such as a lack of activity and exercise, overeating, eating large amounts of junk food and large amounts of preservatives packed tinned foods. It may also be secondary to some hormonal diseases in females like PCOD or thyroid imbalance.";
            case headache:
                return "One of the most common symptoms which is usually indicative of an underlying health  condition. Usually headaches are caused due to Migraine, tension/stress, a very hectic lifestyle and neglecting general health or might be secondary to severe medical conditions such as brain tumour, High Blood Pressure, Hemorrhage etc.";
            case hair:
                return "Hair fall is a common cosmetic complaint in both males and females. It usually occurs due to stress, dry scalp, dandruff, hereditary causes, use of bore-well water, Malnutrition, anemia and a variety of other factors. Hair fall is secondary to thyroid and Pcod problems in females.";
            case men:
                return "Most common problems in men are caused due to hectic schedules and material lifestyles. Some of the very common male specific diseases include Erectile dysfunction, infertility , hair fall. These problems are looked over and it becomes difficult to get treated due to lack of privacy and identity crises.";
            case piles:
                return "Piles is caused due to engorged veins in the anal canal due to constipated stools. It's usually a result of faulty food habits like excess of Non-vegetarian or fatty food. An excess of Spicy foods also contribute to this. Other factors include a low fibre diet, less water consumption on a daily basis etc.";
            case respiratoryProblems:
                return "Respiratory problems can range from a simple common cold which is expressed by sneezing, nose blocks, runny nose to a cancer of lungs which in severe cases may result in death. Other common respiratory diseases include Asthma, Bronchitis, Pneumonia which are usually expressed with symptoms such as breathlessness and cough with expectoration.";
            case bonesAndJoints:
                return "In most people bones and joints start causing trouble in the fourth decade of life and can range from mildly irritating to debilitating. It may go away after a few weeks or last for several months. Symptoms include bone pain, stiffness in the joints, degenerative bone diseases like cervical or lumbar spondylosis, osteoarthritis, osteoporosis, gout and accumulation of uric acid in the joints.";
            case depression:
                return "Depression is classified as a mood disorder. It may be described as feelings of sadness, loss, or anger that interfere with a person’s everyday activities. It is a psychological disorder characterized by low mood and inability to concentrate on daily work. It is often controlled by factors like major life events which are unpleasant, stress, secondary to some psychiatric illness, alcoholism, substance abuse. Often leads to negative thoughts and suicidal tendencies.";
            case growth:
                return "Growth is completely dependent on a child's genes and the kind of nutrition they receive in the early stages of life. In later stages it's dependent on their physical activity and the diet they indulge themselves in. The initial delayed growth or growth retardation can be corrected but in such cases prognosis is low in the later stages of life.";
            case heartProblems:
                return "Heart health depends on the lifestyle of an individual and they are usually acquired over prolonged periods. Stress is a major contributor and leads to high blood pressure, eating high quantities of junk food and fat rich foods lead to cholesterol problems which in turn cause heart blockages. Heart diseases include Arrhythmia, Atherosclerosis, Cardiomyopathy, Congenital heart defects etc.";
            case ENT:
                return "ENT consists of Ear, Nose and Throat problems, they are usually precursors of respiratory diseases and should be resolved in the initial stages itself. Associated diseases include Otitis media, Otomycosis, wax, hearing disability for Ears; Common cold, sinusitis, allergic rhinitis, polyps, DNS for Nose; Cough, laryngitis / pharyngitis, throat infections and tonsillitis for Throat.";
            case mouthAndTeeth:
                return "Dental and oral health is an essential part of overall health and well-being, poor oral hygiene can lead to dental cavities and gum disease and has also been linked to heart disease, cancer, and diabetes. Common disease include oral ulcers, abscess, bad breath, ranula, bleeding and swollen gums, common tooth ache, sensitivity of teeth.";
            case children:
                return "A healthy diet helps children grow and develop properly and reduces their risk of chronic diseases, including obesity. Children suffer from various health issues such as delayed milestones, Teething issues, Chickenpox, Measles, Mumps, otitis media, Tonsillitis. Ill effects of vaccinations are the usual children related problems that are seen and can be easily treated with our homeopathic medicines.";
            case nutritionAndHealth:
                return " Food and nutrition choices make a huge impact on how you feel today, tomorrow and what the future holds in terms of promoting and maintaining good health. Common problems associated with poor nutrition include Mal absorption, Marasmus and Kwashiorkor, Lactose Intolerance etc. These can be resolved with strict diet and regimen with proper monitoring and suitable medication.";
            case maternal:
                return "Maternal illnesses increase the chance that your baby will be born with a birth defect or have a chronic health problem. Diabetes, cytomegalovirus, toxoplasmosis and Strep B are just a few of the illnesses that can cause an adverse outcome to pregnancy. Disorders like Hyperemesis Gravidarum, loss of appetite, morning sickness, gestational diabetes, hypo/hyper Thyroidism, HTN etc can be resolved with simple medicines.";
            case others:
                return "Couldn't find the disease you were searching for? Enter your health issues down below. Homeopathy is used for treating an extremely wide range of health conditions and other general diseases like gastritis, generalised body pains, Fever, Anaemia, Varicose veins, Constipation or diarrhoea, Appendicitis, fracture, Injury and many more.";

            default:
                return "No information could be found on the particular disease.";
        }
    }

    public int getDrawable() {
        switch(disease) {
            case thyroid:
                return R.drawable.disease_thyroid;
            case diabetes:
                return R.drawable.disease_diabetes;
            case skin:
                return R.drawable.disease_skin;
            case female:
                return R.drawable.disease_female;
            case renalProblems:
                return R.drawable.disease_renal;
            case weightLossAndGain:
                return R.drawable.disease_weight;
            case headache:
                return R.drawable.disease_headache;
            case hair:
                return R.drawable.disease_hair;
            case men:
                return R.drawable.disease_male;
            case piles:
                return R.drawable.disease_piles;
            case respiratoryProblems:
                return R.drawable.disease_respiratory;
            case bonesAndJoints:
                return R.drawable.disease_bone;
            case depression:
                return R.drawable.disease_depression;
            case growth:
                return R.drawable.disease_growth;
            case heartProblems:
                return R.drawable.disease_heart;
            case ENT:
                return R.drawable.disease_ent;
            case mouthAndTeeth:
                return R.drawable.disease_mouth;
            case children:
                return R.drawable.disease_children;
            case nutritionAndHealth:
                return R.drawable.disease_nutrition;
            case maternal:
                return R.drawable.disease_maternal;

            default:
                return R.drawable.disease_others;
        }
    }

}
