package hu.bme.crysys.thesis.data.processor;

import hu.bme.crysys.thesis.data.processor.googlePlay.categoryMerger;
import hu.bme.crysys.thesis.data.processor.googlePlay.redundancyRemover;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        /* Merging data from categories to a single collection */
        //categoryMerger categoryMerger = new categoryMerger();
        //categoryMerger.merge("google-play-test");

        /* Removes redundancy from Google Play dataset */
        //redundancyRemover redundancyRemover = new redundancyRemover();
        //redundancyRemover.run("google-play-test","_MERGED");

        Searcher searcher = new Searcher();

        ArrayList<String> microsoftDevs = searcher.microsoftDevelopers("microsoft-store");
        int countMsGoogleCrossDevs = searcher.countCrossDevelopers(microsoftDevs,"google-play-test","_MERGED_filtered");
    }
}
