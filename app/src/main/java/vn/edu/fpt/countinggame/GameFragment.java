package vn.edu.fpt.countinggame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GameFragment extends Fragment {

    private List<Integer> numberList; // flag file names

    private int guessRows; // number of rows displaying guess Buttons
    private Handler handler; // used to delay loading next flag
    private LinearLayout[] guessLinearLayouts; // rows of answer Buttons
    private TextView timerTextView;
    private long startTime;
    private long totalSecondsElapsed = 0;
    private boolean isRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        System.out.println("onCreateView");
        handler = new Handler(Looper.getMainLooper());
        timerTextView = view.findViewById(R.id.time);
        guessLinearLayouts = new LinearLayout[3];
        guessLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.row3LinearLayout);
        // configure listeners for the guess Buttons
        int numberRow = 0;
        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                System.out.println("number");
                System.out.println(numberRow * 3 + column + 1);
                button.setText("2");
                button.setOnClickListener(guessButtonListener);

            }
            numberRow++;
        }
        Button buttonReset = view.findViewById(R.id.buttonRePlay);
        buttonReset.setOnClickListener(resetButtonListener);
        return view; // returns the fragment's view for display
    }

    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetGame();
        }
    };
    private View.OnClickListener guessButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("OnClickListener");
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            if (guess != "") {
                return;
            }
            // Lấy ID dạng int
            int buttonIdInt = guessButton.getId();
            // Chuyển đổi ID từ int sang tên tài nguyên dạng String
            String buttonIdString = "";
            try {
                buttonIdString = getResources().getResourceEntryName(buttonIdInt);
            } catch (android.content.res.Resources.NotFoundException e) {
                // Xử lý trường hợp không tìm thấy tài nguyên với ID này (ít xảy ra với ID từ XML)
                e.printStackTrace();
                buttonIdString = "ID không tìm thấy hoặc không hợp lệ";
            }
            String numberString = buttonIdString.substring("button".length());
            int valueNumber = numberList.get(Integer.parseInt(numberString) - 1);
            String valueString = String.valueOf(valueNumber);
            guessButton.setText(valueString);

            setEnableBox(false);

            int numberBoxOpen = getBoxOpen();
            if (numberBoxOpen == numberList.toArray().length) {
                handler.removeCallbacks(timerRunnable); // Dừng Runnable
                isRunning = false;
                String message = getString(R.string.results, totalSecondsElapsed);

                new AlertDialog.Builder(getActivity()).setMessage(message)
                        .setPositiveButton(R.string.reset_quiz, ((dialog, which) -> resetGame())).show();
            }

            if (numberBoxOpen != valueNumber) {
                // close box
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                closeBox();
                                setEnableBox(true);
                            }
                        }, 500);

            }
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            setEnableBox(true);
                        }
                    }, 500);
        } // end method onClick
    }; // end answerButtonListener

    // get số bài đã mở
    private int getBoxOpen() {
        int totalBoxOpen = 0;
        for (int row = 0; row < guessRows; row++) {
            // place Buttons in currentTableRow
            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {
                // get reference to Button to configure
                Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                String valueBox = newGuessButton.getText().toString();
                if (valueBox != "") totalBoxOpen++;
            }
        }
        return totalBoxOpen;
    }

    // update guessRows based on value in SharedPreferences
    public void updateGuessRows(SharedPreferences sharedPreferences) {
        System.out.println("updateGuessRows");
        String choices = sharedPreferences.getString(MainActivity.CHOICES, null);
        guessRows = Integer.parseInt(choices) / 3;
        numberList = genNumberList(Integer.parseInt(choices));
        for (LinearLayout layout : guessLinearLayouts)
            layout.setVisibility(View.INVISIBLE);
        for (int row = 0; row < guessRows; row++)
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
    }

    public void resetGame() {
        numberList = genNumberList((guessRows * 3));
        // add 3, 6, or 9 guess Buttons based on the value of guessRows
        closeBox();
        startTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu
        handler.postDelayed(timerRunnable, 0); // Bắt đầu chạy ngay lập tức
        timerTextView.setText("00:00");
        isRunning = true;
    }

    private void closeBox() {
        for (int row = 0; row < guessRows; row++) {
            // place Buttons in currentTableRow
            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {
                // get reference to Button to configure
                Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);
                // set Number into button
                newGuessButton.setText("");
            }
        }
    }

    // Runnable để cập nhật UI mỗi giây
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            totalSecondsElapsed = millis / 1000; // Cập nhật tổng số giây

            int minutes = (int) (totalSecondsElapsed / 60);
            int seconds = (int) (totalSecondsElapsed % 60);

            timerTextView.setText(String.format(Locale.getDefault(), "Thời gian: %02d:%02d", minutes, seconds));

            // Đặt lịch để Runnable chạy lại sau 1 giây (1000 mili giây)
            handler.postDelayed(this, 1000);
        }
    };

    private void setEnableBox(boolean status) {
        for (int row = 0; row < guessRows; row++) {
            // place Buttons in currentTableRow
            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {
                // get reference to Button to configure
                Button newGuessButton = (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setClickable(status);
            }
        }
    }

    public List<Integer> genNumberList(int total) {
        List<Integer> numbers = new ArrayList<>();
        // Add numbers from 1 to total to the list
        for (int i = 1; i <= total; i++) {
            numbers.add(i);
        }

        // Shuffle the list
        Collections.shuffle(numbers); // Uses a default random source
        // If you need a specific seed for reproducibility, you can use:
        // Collections.shuffle(numbers, new Random(someSeed));

        return numbers;
    }
}
