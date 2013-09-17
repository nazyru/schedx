package com.nazir.schedx.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.model.Todo;
import com.nazir.schedx.persist.TodosHelper;
import com.nazir.schedx.util.DateTimeHelper;

import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;

public class TodoDetailFragment extends MyCustomFragment {
	private TextView nameView;
	private TextView memoView;
	private TextView timeView;
	private RatingBar ratingBar;

	@Override
	public void onStart() {
		super.onStart();
		
		int id = getArguments().getInt(ID);
		TodosHelper helper = new TodosHelper(getSherlockActivity());
		Todo todo = helper.getTodo(id);
		helper.disconnect();
		
		nameView = (TextView) getSherlockActivity().findViewById(R.id.todo_detail_name_view);
		memoView = (TextView) getSherlockActivity().findViewById(R.id.todo_detail_memo_view);
		timeView = (TextView) getSherlockActivity().findViewById(R.id.todo_detail_time_view);
		ratingBar = (RatingBar) getSherlockActivity().findViewById(R.id.todo_detail_rating_view);
		
		nameView.setText(todo.getName());
		memoView.setText(todo.getDescription());
		timeView.setText(DateTimeHelper.getDisplayableDate(todo.getTime()));
		ratingBar.setRating(todo.getRating());
		
	}
	
	@Override
	public View onCreateView(LayoutInflater layoutinflater,
			ViewGroup viewgroup, Bundle bundle) {
		
		return layoutinflater.inflate(R.layout.todo_detail_layout, null, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
		super.onCreateOptionsMenu(menu, menuinflater);
		
		menu.removeItem(R.id.action_item_new);
	}

}
