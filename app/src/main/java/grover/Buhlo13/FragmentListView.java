package grover.Buhlo13;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ndv on 19.09.2016.
 */
public class FragmentListView extends Fragment{
    private DbToList mDbToList;
    private ListView mListView;
    Boolean isSumSelect = false;
    Boolean isVerboseListView = false;
    MyCursorAdapter myCursorAdapter;
    MatrixCursor mMatrixCursor;
    Cursor mCursor;
    private static final String ARG_SECTION_NUMBER = "section_number";


    //Cursor mCursor;

    CrutDB mCrutDB;

    //private static final String ARG_SECTION_NUMBER = "section_number";


    public static FragmentListView newInstance(int sectionNumber) {
        FragmentListView fragment = new FragmentListView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentListView(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_listview, container, false);

        mCrutDB = new CrutDB(getActivity());

        mListView = (ListView) rootView.findViewById(R.id.list);
//        MergeCursor mergeCursor = new MergeCursor(new Cursor[] {mCrutDB.selectByMonth(), mCrutDB.selectGroupByYear()});

        myCursorAdapter = new MyCursorAdapter(getActivity(), mCrutDB.selectByMonth());
        mListView.setAdapter(myCursorAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                        myCursorAdapter.changeCursor(mCrutDB.selectVerboseCurrentMonthGroupByName(nameForSelect));
                        isVerboseListView = true;                    }
                } else if ((isSumSelect)&&(!isVerboseListView)){
                    TextView textViewDate = (TextView)view.findViewById(R.id.textViewDate);
                    String year = textViewDate.getText().toString();
                    myCursorAdapter.changeCursor(mCrutDB.selectVerboseYearGroupByMonth(year));
                    isVerboseListView = true;
//                    Snackbar.make(view, "verbose sum", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                @Override
                                                public void onRefresh() {

//Log.d("----->", "refresh");
                                                    if (!isSumSelect) {

//mCursorAdapterMonth.bindView(mListView, getActivity(),);
                                                        myCursorAdapter.changeCursor(mCrutDB.selectGroupByYear());
                                                        isSumSelect = true;
// mDbToList.getDBDataToListView(mCrutDB, mListView, getActivity(), CrutDB.Select.month_sum);

                                                    } else {
                                                        myCursorAdapter.changeCursor(mCrutDB.selectByMonth());
                                                        isSumSelect = false;
                                                    }
                                                    isVerboseListView = false;
                                                    swipeRefreshLayout.setRefreshing(false);

                                                }
                                            });
        //getDBDataToListView(mCrutDB, mListView , );



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
