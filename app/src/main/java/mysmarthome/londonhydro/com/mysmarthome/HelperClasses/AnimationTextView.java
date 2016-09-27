package mysmarthome.londonhydro.com.mysmarthome.HelperClasses;

import android.widget.TextView;


public class AnimationTextView extends Thread {

    public TextView txtToAnimate;
    public int totalProgress;
    private int counter = 0;

public AnimationTextView(TextView txt2Animate, int totalProg){
    txtToAnimate=txt2Animate;
    totalProgress=totalProg;
}
    @Override
    public void run() {
        super.run();
        while (counter < totalProgress) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            txtToAnimate.post(new Runnable() {

                public void run() {
                    txtToAnimate.setText("" + counter);

                }
            });
            counter++;
        }

    }

}
