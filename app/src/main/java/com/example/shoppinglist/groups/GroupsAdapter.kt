package com.example.shoppinglist.groups

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_profile_view.view.*

class GroupsAdapter(val context: Context?, val ref: DatabaseReference, private val userList: ArrayList<User>, var currentUserData: User?): RecyclerView.Adapter<GroupsViewHolder>(){

    private val usersReference = ref
    private val groupsReference = FirebaseDatabase.getInstance().getReference("GROUPS")
    private var currentUserDisplayName = FirebaseAuth.getInstance().currentUser?.displayName.toString()
    private var mChildEventListener: ChildEventListener

    private val childEventListener = object :  ChildEventListener {
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            val changedUserCurrentData = p0.getValue(User::class.java)

            if (!userList.contains(changedUserCurrentData)){
                userList.add(changedUserCurrentData!!)
            } else {
                userList.set(
                    userList.indexOf(changedUserCurrentData),
                    changedUserCurrentData!!
                )
            }
            notifyDataSetChanged()
//            var previousElementIndex = 0
//            if (p1 == null){
//                previousElementIndex = -1
//            } else {
//                for (i in 0..userList.size - 1) {
//                    if (userList[i].displayName == p1) {
//                        previousElementIndex = i
//                        break
//                    }
//                }
//            }
//            val changedUserPreviousData = userList.elementAt(previousElementIndex + 1)
//            if (userList.contains(changedUserPreviousData)) {
//                userList.set(previousElementIndex + 1, changedUserCurrentData!!)
//                notifyItemChanged(previousElementIndex + 1)
//            }
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                if (user != null && user.sharedGroupName == currentUserData!!.sharedGroupName && !userList.contains(user)) {
                    userList.add(user)
                    notifyItemInserted(userList.size - 1)
                    //notifyDataSetChanged()
                }
            }

        override fun onChildRemoved(p0: DataSnapshot) {
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    init {
        val currentUserValueEventListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUserData = p0.getValue(User::class.java)
                //usersReference.child(currentUserData!!.displayName).child("isOnline").setValue(true)
                usersReference.addChildEventListener(childEventListener)
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        }
        usersReference.child(currentUserDisplayName).addListenerForSingleValueEvent(currentUserValueEventListener)
        mChildEventListener = childEventListener
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        val user = userList[position]

        holder.itemView.display_name_label.text = user.displayName
        holder.itemView.email_label.text = user.email
        holder.itemView.userProfilePicture.setImageResource(user.avatar)
        if (user.online){
            holder.itemView.onlinePresenceImage.setImageResource(android.R.drawable.presence_online)
        } else {
            holder.itemView.onlinePresenceImage.setImageResource(android.R.drawable.presence_offline)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_profile_view, parent, false)
            )
    }
    override fun getItemCount() = userList.size

    fun cleanupListener() {
        if (mChildEventListener != null) {
            usersReference.removeEventListener(mChildEventListener)
        }
    }
}