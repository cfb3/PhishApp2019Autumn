package instructor.tcss450.uw.edu.phishapp2019autumn.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import instructor.tcss450.uw.edu.phishapp2019autumn.R;
import instructor.tcss450.uw.edu.phishapp2019autumn.model.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlogPostFragment extends Fragment {


    public BlogPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_post, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null && getArguments().containsKey(getString(R.string.key_blog))) {

            //Get the blog post object from arguments
            BlogPost b = (BlogPost) getArguments().getSerializable(getString(R.string.key_blog));




            //Because the white list kept the <p> tags, there is still html in the string. So
            //you still want to use Html.fromHtml
            ((TextView) getActivity().findViewById(R.id.text_blog_preview))
                    .setText(Html.fromHtml(
                            b.getTeaser(),
                            Html.FROM_HTML_MODE_LEGACY));




            ((TextView) getActivity().findViewById(R.id.text_blog_title))
                    .setText(b.getTitle());

            ((TextView) getActivity().findViewById(R.id.text_blog_pubdate))
                    .setText(b.getPubDate());


//            getActivity().findViewById(R.id.button_blog_url).setOnClickListener(v ->
//                    mListener.onFragmentInteraction(Uri.parse(b.getUrl())));
        }


    }

}
