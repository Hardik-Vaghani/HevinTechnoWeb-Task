package com.hardik.hevintechnowebtask.presentation.ui.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardik.hevintechnowebtask.MainActivity
import com.hardik.hevintechnowebtask.R
import com.hardik.hevintechnowebtask.adapter.UserAdapter
import com.hardik.hevintechnowebtask.data.database.dao.SortField
import com.hardik.hevintechnowebtask.data.database.dao.SortOrder
import com.hardik.hevintechnowebtask.databinding.FragmentHomeBinding
import com.hardik.hevintechnowebtask.presentation.MainViewModel


class HomeFragment : Fragment() {
    private val TAG = HomeFragment::class.java.simpleName

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel
    lateinit var userAdapter: UserAdapter


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).mainViewModel
        arguments?.let {}
    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
                             ) : View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Enable the options menu
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        setUpRecyclerView()
        userAdapter.setOnItemClickListener {}

        viewModel.state.observe(viewLifecycleOwner) { userListState ->
            // Update UI based on the state
            if (userListState.isLoading) {
                // Show loading indicator
                showProgressBar()
            } else if (userListState.error.isNotEmpty()) {
                // Show error message
                Toast.makeText(requireContext(), userListState.error, Toast.LENGTH_SHORT).show()
                hideProgressBar()
            } else {
                // Update UI with the user list
                val users = userListState.users
                hideProgressBar()
//                userAdapter.differ.submitList(users.toList())
                userAdapter.setOriginalList(users.toList())
                binding.recyclerview.setPadding(0, 0, 0, 0)
            }
        }

        val searchIcon = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        // Set icon color programmatically
        searchIcon.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.darker_gray), PorterDuff.Mode.SRC_IN)
        // Set text size programmatically
        val searchAutoComplete = binding.searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
        searchAutoComplete.textSize = resources.getDimension(com.intuit.ssp.R.dimen._6ssp) // Set your desired text size
        // Set text color programmatically
        searchAutoComplete.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // if query is done then enter the search
//                if (query!= null){
//                    userAdapter.filter.filter(query)
//                }else{
//                    userAdapter.filter.filter("")
//                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query is add char then the search
                userAdapter.filter.filter(newText)
//                if (newText.isNullOrBlank()) {
//                    // Show the search icon when the query is empty
//                    searchIcon.visibility = View.VISIBLE
//                } else {
//                    // Hide the search icon when there is text in the query
//                    searchIcon.visibility = View.GONE
//                }
                return true
            }

        })

        /*  // Collecting the StateFlow
        lifecycleScope.launch {
            viewModel.state1.collect { userListState ->
                // Update UI based on the state
                if (userListState.isLoading) {
                    // Show loading indicator
                    showProgressBar()
                } else if (userListState.error.isNotEmpty()) {
                    // Show error message
                    Toast.makeText(requireContext(), userListState.error, Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                } else {
                    // Update UI with the user list
                    val users = userListState.users
                    hideProgressBar()
                    userAdapter.differ.submitList(users.toList())
                    binding.recyclerview.setPadding(0, 0, 0, 0)
                }
            }
        }
        viewModel.fetchUsers()*/
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotABeginning = firstVisibleItemPosition >= 0
//            val totalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotABeginning && isScrolling //&& totalMoreThanVisible
            if (shouldPaginate) {
                Log.d(TAG, "onScrolled: call new Api data")
                viewModel.getUsers()
                isScrolling = false
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_default -> {
                viewModel.getSortedUsers(sortField = SortField.ID)
                true
            }
            R.id.action_sort_name -> {
                viewModel.getSortedUsers(sortField = SortField.FIRST_NAME)
                true
            }
            R.id.action_sort_name_desc -> {
                viewModel.getSortedUsers(sortField = SortField.FIRST_NAME, sortOrder = SortOrder.DESC)
                true
            }
            R.id.action_sort_email -> {
                viewModel.getSortedUsers(sortField = SortField.EMAIL)
                true
            }
            R.id.action_sort_email_desc -> {
                viewModel.getSortedUsers(sortField = SortField.EMAIL, sortOrder = SortOrder.DESC)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerView() {
        userAdapter = UserAdapter()
        binding.recyclerview.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}