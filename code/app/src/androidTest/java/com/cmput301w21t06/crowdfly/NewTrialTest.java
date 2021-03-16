package com.cmput301w21t06.crowdfly;


import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Views.EditBinomialTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditCountTrialFragment;
import com.cmput301w21t06.crowdfly.Views.EditMeasureTrialFragment;
import com.robotium.solo.Solo;


public class NewTrialTest implements EditBinomialTrialFragment.OnFragmentInteractionListener, EditCountTrialFragment.OnFragmentInteractionListener, EditMeasureTrialFragment.OnFragmentInteractionListener{

    private Solo solo;



    @Override
    public void onOkPressed(BinomialTrial trial) {

    }

    @Override
    public void onOkPressed(CountTrial trial) {

    }

    @Override
    public void onOkPressed(MeasurementTrial trial) {

    }
}
