package vn.edu.fpt.countinggame;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameFragment extends Fragment {


    // String used when logging error messages
    private static final String TAG = "FlagQuiz Activity";

    private static final int FLAGS_IN_QUIZ = 10;

    private List<String> fileNameList; // flag file names
    private List<String> quizCountriesList; // countries in current quiz
    private Set<String> regionsSet; // world regions in current quiz
    private String correctAnswer; // correct country for the current flag
    private int totalGuesses; // number of guesses made
    private int correctAnswers; // number of correct guesses
    private int guessRows; // number of rows displaying guess Buttons
    private SecureRandom random; // used to randomize the quiz
    private Handler handler; // used to delay loading next flag
    private Animation shakeAnimation; // animation for incorrect guess

    private TextView questionNumberTextView; // shows current question #
    private ImageView flagImageView; // displays a flag
    private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
    private TextView answerTextView; // displays Correct! or Incorrect!

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_game, container, false);

        fileNameList = new ArrayList<String>();
        quizCountriesList = new ArrayList<String>();
        random = new SecureRandom();
        handler = new Handler();

        // load the shake animation that's used for incorrect answers
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        // get references to GUI components
//        questionNumberTextView =
//                (TextView) view.findViewById(R.id.questionNumberTextView);
//        flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
        guessLinearLayouts = new LinearLayout[3];
        guessLinearLayouts[0] =
                (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] =
                (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] =
                (LinearLayout) view.findViewById(R.id.row3LinearLayout);
//        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        // configure listeners for the guess Buttons
        for (LinearLayout row : guessLinearLayouts)
        {
            for (int column = 0; column < row.getChildCount(); column++)
            {
                Button button = (Button) row.getChildAt(column);
                button.setText("2");
                button.setOnClickListener(guessButtonListener);
            }
        }
        return view; // returns the fragment's view for display
    }

    private View.OnClickListener guessButtonListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            System.out.println("giá trị button");
            System.out.println(guess);
////            String answer = getCountryName(correctAnswer);
//            ++totalGuesses; // increment number of guesses the user has made
//
//            if (guess.equals(1)) // if the guess is correct
//            {
//                ++correctAnswers; // increment the number of correct answers
//
//                // display correct answer in green text
////                answerTextView.setText(answer + "!");
////                answerTextView.setTextColor(
////                        getResources().getColor(R.color.correct_answer));
//
//                disableButtons(); // disable all guess Buttons
//
//                // if the user has correctly identified FLAGS_IN_QUIZ flags
//                if (correctAnswers == FLAGS_IN_QUIZ)
//                {
//                    new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.results,totalGuesses,(1000/(double) totalGuesses)))
//                            .setPositiveButton(R.string.reset_quiz, ((dialog, which) -> resetQuiz())).show();
//                }
//                else // answer is correct but quiz is not over
//                {
//                    // load the next flag after a 1-second delay
//                    handler.postDelayed(
//                            new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
////                                    loadNextFlag();
//                                    System.out.println("next question");
//                                }
//                            }, 2000); // 2000 milliseconds for 2-second delay
//                }
//            }
//            else // guess was incorrect
//            {
//                flagImageView.startAnimation(shakeAnimation); // play shake
//            }
        } // end method onClick
    }; // end answerButtonListener


    // update guessRows based on value in SharedPreferences
    public void updateGuessRows(SharedPreferences sharedPreferences)
    {
        // get the number of guess buttons that should be displayed
        String choices =
                sharedPreferences.getString(MainActivity.CHOICES, null);
        //set data
        guessRows = Integer.parseInt(choices) / 3;

        // hide all guess button LinearLayouts
        for (LinearLayout layout : guessLinearLayouts)
            layout.setVisibility(View.INVISIBLE);

        // display appropriate guess button LinearLayouts
        for (int row = 0; row < guessRows; row++)
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
    }


    // set up and start the next quiz
    public void resetGame()
    {
        // use AssetManager to get image file names for enabled regions
        AssetManager assets = getActivity().getAssets();
        fileNameList.clear(); // empty list of image file names
//
//        correctAnswers = 0; // reset the number of correct answers made
//        totalGuesses = 0; // reset the total number of guesses the user made
        quizCountriesList.clear(); // clear prior list of quiz countries
//
//        int flagCounter = 1;
//        int numberOfFlags = fileNameList.size();
//
//        // add FLAGS_IN_QUIZ random file names to the quizCountriesList

//        loadNextFlag(); // start the quiz by loading the first flag

    } // end method reset_quiz



//    private void disableButtons()
//    {
//        for (int row = 0; row < guessRows; row++)
//        {
//            LinearLayout guessRow = guessLinearLayouts[row];
//            for (int i = 0; i < guessRow.getChildCount(); i++)
//                guessRow.getChildAt(i).setEnabled(false);
//        }
//    }
}
