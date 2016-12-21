package grover.Buhlo13;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ndv on 20.12.2016.
 */
public class FragmentTwoListView extends Fragment {
    private DbToList mDbToList;
    private ListView mListViewMonth, mListViewYear;
    Boolean isSumSelect = false;
    Boolean isVerboseListView = false;
    MyCursorAdapter mCursorAdapterMonth, mCursorAdapterYear;
    Cursor mCursor;
    private static final String ARG_SECTION_NUMBER = "section_number";


    //Cursor mCursor;

    CrutDB mCrutDB;

    //private static final String ARG_SECTION_NUMBER = "section_number";


    public static FragmentTwoListView newInstance(int sectionNumber) {
        FragmentTwoListView fragment = new FragmentTwoListView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentTwoListView() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_two_listview, container, false);

        mCrutDB = new CrutDB(getActivity());

//        mListViewYear = (ListView) rootView.findViewById(R.id.listYear);
//        mCursorAdapterYear = new MyCursorAdapter(getActivity(), mCrutDB.selectGroupByYear());
//        mListViewYear.setAdapter(mCursorAdapterYear);

        mListViewMonth = (ListView) rootView.findViewById(R.id.listMonth);
        mCursorAdapterMonth = new MyCursorAdapter(getActivity(), mCrutDB.selectByMonth());
        mListViewMonth.setAdapter(mCursorAdapterMonth);

        mListViewMonth.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //if not sum select displaiys on fraagment
                int intId = (int)(long)id;
                if (!isSumSelect) {
                    if (isVerboseListView) {
                        popUpSnackbar(getView(), intId);
                        //select by name
                    } else {
                        TextView textView = (TextView) view.findViewById(R.id.textViewName);
                        String nameForSelect = textView.getText().toString();
//                        mCursorAdapterMonth.changeCursor(mCrutDB.selectByName(nameForSelect));
                        mCursorAdapterMonth.changeCursor(mCrutDB.selectVerboseCurrentMonthGroupByName(nameForSelect));
                        isVerboseListView = true;                    }
                } else if ((isSumSelect)&&(!isVerboseListView)){
                    TextView textViewDate = (TextView)view.findViewById(R.id.textViewDate);
                    String year = textViewDate.getText().toString();
                    mCursorAdapterMonth.changeCursor(mCrutDB.selectVerboseYearGroupByMonth(year));
                    isVerboseListView = true;
//                    Snackbar.make(view, "verbose sum", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefreshTwo);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCursorAdapterMonth.changeCursor(mCrutDB.selectByMonth());
//                mCursorAdapterYear.changeCursor(mCrutDB.selectGroupByYear());
                isVerboseListView = false;
                swipeRefreshLayout.setRefreshing(false);

            }
            
        });

        return rootView;
    }


    void popUpSnackbar(final View view, final int idForDelete) {
        final Snackbar snackbar = Snackbar.make(view, R.string.DeleteItem, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.ConfirmDeleteButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrutDB.deleteRow(idForDelete);
                snackbar.dismiss();
                snackbar.make(view, "удалено", Snackbar.LENGTH_SHORT).show();

            }
        });
        snackbar.show();
        //Snackbar.make(view, "add", Snackbar.LENGTH_SHORT).show();
    }
    public void longClickListener(){

    }
}
