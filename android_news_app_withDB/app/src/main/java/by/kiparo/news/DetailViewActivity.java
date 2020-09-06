package by.kiparo.news;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import static by.kiparo.news.MainActivity.IMAGE_URL;
import static by.kiparo.news.MainActivity.STORY_URL;
import static by.kiparo.news.MainActivity.SUMMARY;
import static by.kiparo.news.MainActivity.TITLE;

public class DetailViewActivity extends Activity {
    private String storyURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        storyURL = extras.getString(STORY_URL);
        String title = extras.getString(TITLE);
        String summary = extras.getString(SUMMARY);
        String imageURL = extras.getString(IMAGE_URL);

        TextView titleView = (TextView) findViewById(R.id.title);
        DraweeView imageView = (DraweeView) findViewById(R.id.news_image);
        TextView summaryView = (TextView) findViewById(R.id.summary_content);

        titleView.setText(title);
        summaryView.setText(summary);
        if (imageURL != null){
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequest.fromUri(Uri.parse(imageURL)))
                    .setOldController(imageView.getController()).build();
            imageView.setController(draweeController);
        }
    }

    public void onFullStoryClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(storyURL));
        startActivity(intent);
    }
}
