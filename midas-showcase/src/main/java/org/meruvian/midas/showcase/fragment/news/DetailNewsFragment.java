package org.meruvian.midas.showcase.fragment.news;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.meruvian.midas.core.defaults.DefaultFragment;
import org.meruvian.midas.core.service.TaskService;
import org.meruvian.midas.showcase.R;
import org.meruvian.midas.showcase.entity.News;
import org.meruvian.midas.showcase.task.news.NewsDetailGet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;


/**
 * Created by ludviantoovandi on 25/10/14.
 */
public class DetailNewsFragment extends DefaultFragment implements TaskService<News> {
    @Bind({R.id.text_title, R.id.text_date, R.id.text_content})
    List<TextView> textViews;

    private ProgressDialog dialog;

    private NewsDetailGet newsDetailGet;

    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    protected int layout() {
        return R.layout.fragment_news_detail;
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
//
//        title = (TextView) view.findViewById(R.id.text_title);
//        date = (TextView) view.findViewById(R.id.text_date);
//        content = (TextView) view.findViewById(R.id.text_content);
//
//        return view;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        newsDetailGet = new NewsDetailGet(this, getActivity());
        newsDetailGet.execute(getArguments().getString("news_id", ""));
    }

    @Override
    public void onExecute(int code) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getActivity().getString(R.string.waiting));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                newsDetailGet.cancel(true);
            }
        });
        dialog.show();
    }

    @Override
    public void onSuccess(int code, News news) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        textViews.get(0).setText(news.getTitle());
        textViews.get(1).setText(dateFormat.format(news.getLogInformation().getCreateDate()));
        textViews.get(2).setText(news.getContent());
    }

    @Override
    public void onCancel(int code, String message) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Toast.makeText(getActivity(), getString(R.string.cancle), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int code, String message) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Toast.makeText(getActivity(), getString(R.string.failed_recieve_news), Toast.LENGTH_SHORT).show();
    }

}
