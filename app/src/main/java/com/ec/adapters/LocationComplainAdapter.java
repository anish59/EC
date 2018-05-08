package com.ec.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ec.R;
import com.ec.helper.AppConstants;
import com.ec.helper.PrefUtils;
import com.ec.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anish on 06-02-2018.
 */

public class LocationComplainAdapter extends RecyclerView.Adapter<LocationComplainAdapter.MyViewHolder> {
    private OnVoteClick onVoteClick;
    private List<Post> data;
    private Context context;


    public LocationComplainAdapter(Context context, List<Post> data, OnVoteClick onVoteClick) {
        this.context = context;
        this.data = data;
        this.onVoteClick = onVoteClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_complain, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        makeTextViewResizable(holder.txtDisc, 3, "View More", true);
        holder.txtTitle.setText(data.get(position).getTitle());
        holder.txtDisc.setText(data.get(position).getDescription());
//        Glide.with(context).load(AppConstants.GLIDE_BASE_URL + data.get(position).getImage()).into(holder.img);
        Glide.with(context)
                .load(AppConstants.GLIDE_BASE_URL + data.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.no_image_found).error(R.drawable.no_image_found))
                .into(holder.img);
        holder.txtUpVote.setText((data.get(position).getUpVote() == null || (data.get(position).getUpVote().trim().length() == 0)) ? "0" : data.get(position).getUpVote());
        holder.txtDownVote.setText((data.get(position).getDownVote() == null || (data.get(position).getDownVote().trim().length() == 0)) ? "0" : data.get(position).getDownVote());

        holder.txtUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteClick.onVoteClick(PrefUtils.getUser(context).getUserId(), data.get(position).getId(), "1");
            }
        });
        holder.txtDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteClick.onVoteClick(PrefUtils.getUser(context).getUserId(), data.get(position).getId(), "0");
            }
        });
        holder.imgUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteClick.onVoteClick(PrefUtils.getUser(context).getUserId(), data.get(position).getId(), "1");
            }
        });
        holder.imgDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVoteClick.onVoteClick(PrefUtils.getUser(context).getUserId(), data.get(position).getId(), "0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Post> data) {
        this.data = new ArrayList<>();
        this.data = data;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName, txtDisc, txtTitle, txtUpVote, txtDownVote;
        private ImageView img, imgUpVote, imgDownVote;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDisc = (TextView) itemView.findViewById(R.id.txtDisc);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtUpVote = (TextView) itemView.findViewById(R.id.txtUpVote);
            txtDownVote = (TextView) itemView.findViewById(R.id.txtDownVote);
            imgUpVote = (ImageView) itemView.findViewById(R.id.imgUpVote);
            imgDownVote = (ImageView) itemView.findViewById(R.id.imgDownVote);
            img = itemView.findViewById(R.id.img);
        }
    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = false;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#343434"));

        }

        @Override
        public void onClick(View widget) {

        }
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);


        }

        return ssb;

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    public interface OnVoteClick {
        void onVoteClick(String userId, String postId, String vote);
    }

}
