package com.bonepeople.android.sdcardcleaner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonepeople.android.sdcardcleaner.R;
import com.bonepeople.android.sdcardcleaner.models.SDFile;
import com.bonepeople.android.sdcardcleaner.utils.NumberUtil;

/**
 * 文件列表的数据适配器
 * <p>
 * Created by bonepeople on 2017/6/26.
 */

public class Adapter_list_file extends RecyclerView.Adapter<Adapter_list_file.ViewHolder> {
    private Context _context;
    private SDFile _data;

    public Adapter_list_file(Context _context) {
        this._context = _context;
    }

    public void set_data(SDFile _data) {
        this._data = _data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View _view = LayoutInflater.from(_context).inflate(R.layout.item_list_file, parent, false);
        return new ViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SDFile _temp_data = _data.get_children().get(position);
        int _percent = _temp_data.get_sizePercent();
        LinearLayout.LayoutParams _params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, _percent);
        holder._line_start.setLayoutParams(_params);
        _params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 100 - _percent);
        holder._line_end.setLayoutParams(_params);
        float _alpha = (float) NumberUtil.div(_percent, 50, 2);
        holder._line_start.setAlpha(_alpha > 1 ? 1 : _alpha);
        holder._text_name.setText(_temp_data.get_name());
        holder._text_size.setText(Formatter.formatFileSize(_context, _temp_data.get_size()) + " | " + _percent + " | " + _alpha);
    }

    @Override
    public int getItemCount() {
        return _data.get_children().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View _line_start;
        public View _line_end;
        public TextView _text_name;
        public TextView _text_size;

        public ViewHolder(View itemView) {
            super(itemView);
            _line_start = itemView.findViewById(R.id.view_line_start);
            _line_end = itemView.findViewById(R.id.view_line_end);
            _text_name = (TextView) itemView.findViewById(R.id.textview_name);
            _text_size = (TextView) itemView.findViewById(R.id.textview_size);
        }
    }
}
