package com.nazir.schedx.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import static com.nazir.schedx.persist.MySqliteOpenHelper.Todos.*;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.nazir.schedx.R;
import com.nazir.schedx.persist.TodosHelper;
import com.nazir.schedx.remainder.AlarmHelper;

public class TodoListActivity extends MyCustomFragment {
	public static final String TODO_BUNDLE = "com.nazir.schedx.ui.todo";
	private SimpleCursorAdapter adapter;
	private Cursor todoCursor;
	private TodosHelper helper;
	private ListView todoListView;
	private ActionMode mActionMode;

	@SuppressWarnings("deprecation")
	public void onStart() {
		super.onStart();

		todoListView = (ListView) getSherlockActivity().findViewById(
				R.id.todo_list_view);
		helper = new TodosHelper(getSherlockActivity());
		todoCursor = helper.getTodos();
		todoCursor.getColumnIndex(NAME);

		String as[] = { NAME };
		int ai[] = { R.id.todo_list_item };
		adapter = new SimpleCursorAdapter(getSherlockActivity(),
				R.layout.todo_list_layout, todoCursor, as, ai);
		todoListView.setAdapter(adapter);

		todoListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				int id = todoCursor.getInt(todoCursor.getColumnIndex(ID));
				Intent intent = new Intent(getSherlockActivity(),
						TodoDetailActivity.class);
				intent.putExtra(ID, id);
				startActivity(intent);
			}
		});

		initActionMode();
	}

	protected void doDelete() {
		final int id = todoCursor.getInt(todoCursor.getColumnIndex("_id"));
		new AlertDialog.Builder(getSherlockActivity())
				.setTitle(R.string.alert_dialog_title)
				.setMessage(R.string.alert_dilog_message)
				.setIcon(R.drawable.alert)
				.setPositiveButton(R.string.alert_dialog_yes_option,
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialoginterface, int i) {
								helper.delete(id);
								AlarmHelper.cancelTodoAlarm(id,
										getSherlockActivity());
								adapter.notifyDataSetChanged();
								Toast.makeText(getSherlockActivity(),
										"Deleted", Toast.LENGTH_SHORT).show();
							}

						})
				.setNegativeButton(R.string.alert_dialog_no_option,
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialoginterface, int i) {
								dialoginterface.dismiss();
							}
						}).show();
	}

	protected void doEdit() {
		int i = todoCursor.getInt(todoCursor.getColumnIndex(ID));
		Intent intent = new Intent(getSherlockActivity(), TodoActivity.class);
		intent.setAction(Intent.ACTION_EDIT);
		Bundle bundle = new Bundle();
		bundle.putInt(ID, i);
		intent.putExtra(TODO_BUNDLE, bundle);
		startActivity(intent);
	}

	private void initActionMode() {
		final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

			public boolean onActionItemClicked(ActionMode actionmode,
					MenuItem menuitem) {
				switch (menuitem.getItemId()) {
				case R.id.action_delete_item:
					doDelete();
					actionmode.finish();
					break;
				case R.id.edit_schedule_action_item:
					doEdit();
					actionmode.finish();
					break;
				default:
					return false;
				}

				return true;

			}

			public boolean onCreateActionMode(ActionMode actionmode, Menu menu) {
				actionmode.getMenuInflater().inflate(R.menu.context_menu, menu);
				return true;
			}

			public void onDestroyActionMode(ActionMode actionmode) {
				mActionMode = null;
			}

			public boolean onPrepareActionMode(ActionMode actionmode, Menu menu) {
				return false;
			}

		};
		todoListView
				.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> adapterview,
							View view, int i, long l) {
						if (mActionMode != null) {
							return false;
						} else {
							mActionMode = getSherlockActivity()
									.startActionMode(mActionModeCallback);
							return true;
						}
					}
				});
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
		menuinflater.inflate(R.menu.todo_list_menu, menu);
		super.onCreateOptionsMenu(menu, menuinflater);
	}

	public View onCreateView(LayoutInflater layoutinflater,
			ViewGroup viewgroup, Bundle bundle) {
		return layoutinflater.inflate(R.layout.todo_list, null, false);
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		switch (menuitem.getItemId()) {
		case R.id.action_item_new:
			startActivity(new Intent(getSherlockActivity(), TodoActivity.class));
			break;
		case R.id.done_tasks:
			todoCursor = new TodosHelper(getSherlockActivity()).getDoneTasks();
			adapter.changeCursor(todoCursor);
			adapter.notifyDataSetChanged();
			break;
		default:
			return super.onOptionsItemSelected(menuitem);
		}
		return true;
	}

	public void onDestroy() {
		super.onDestroy();
		if (todoCursor != null)
			todoCursor.close();
		if (helper != null)
			helper.disconnect();
	}
}
