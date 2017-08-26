package org.telegram.Adel.OnlineContacts;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.telegram.Adel.Olds.BaseSectionsAdapter;
import org.telegram.Adel.Olds.ChipSpan;
import org.telegram.Adel.Olds.LetterSectionsListView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.R;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ChannelCreateActivity;
import org.telegram.ui.ChannelIntroActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ContactAddActivity;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.GroupCreateActivity.GroupCreateActivityDelegate;
import org.telegram.ui.GroupInviteActivity;

public class OnlineContactsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int add_button = 1;
    private static final int delete = 3;
    private static final int done_button = 10;
    private static final int refresh = 2;
    private static final int search_button = 0;
    private static final int select_all_button = 11;
    private boolean                             allowBots;
    private boolean                             allowUsernameSearch;
    private int                                 chat_id;
    private boolean                             createSecretChat;
    private boolean                             creatingChat;
    private ContactsActivityDelegate            delegate;
    private boolean                             destroyAfterSelect;
    private TextView                            emptyTextView;
    private HashMap<Integer, User>              ignoreUsers;
    private LetterSectionsListView              listView;
    private BaseSectionsAdapter                 listViewAdapter;
    private ContactsActivityMultiSelectDelegate multiSelectDelegate;
    private boolean                             multiSelectMode;
    private boolean                             needForwardCount;
    private boolean                             needPhonebook;
    private boolean                             onlyOnlines;
    private boolean                             onlyUsers;
    ProgressDialog progressDialog;
    private boolean                     returnAsResult;
    private OnlineContactsSearchAdapter searchListViewAdapter;
    private boolean                     searchWas;
    private boolean                     searching;
    private String                      selectAlertString;
    private HashMap<Integer, ChipSpan>  selectedContacts;

    public interface ContactsActivityDelegate {
        void didSelectContact(User user, String str);
    }

    public interface ContactsActivityMultiSelectDelegate {
        void didSelectContacts(List<Integer> list, String str);
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.10 */
    class AnonymousClass10 implements OnClickListener {
        final /* synthetic */ ArrayList val$selectedContacts;

        AnonymousClass10(ArrayList arrayList) {
            this.val$selectedContacts = arrayList;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            ArrayList arrayList = new ArrayList();
            Iterator it = this.val$selectedContacts.iterator();
            while (it.hasNext()) {
                arrayList.add(MessagesController.getInstance().getUser((Integer) it.next()));
            }
            ContactsController.getInstance().deleteContact(arrayList);
            if (OnlineContactsActivity.this.listViewAdapter != null) {
                OnlineContactsActivity.this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.12 */
    class AnonymousClass12 implements TextWatcher {
        final /* synthetic */ EditText val$editTextFinal;

        AnonymousClass12(EditText editText) {
            this.val$editTextFinal = editText;
        }

        public void afterTextChanged(Editable editable) {
            try {
                String obj = editable.toString();
                if (obj.length() != 0) {
                    int intValue = Utilities.parseInt(obj).intValue();
                    if (intValue < 0) {
                        this.val$editTextFinal.setText("0");
                        this.val$editTextFinal.setSelection(this.val$editTextFinal.length());
                    } else if (intValue > 300) {
                        this.val$editTextFinal.setText("300");
                        this.val$editTextFinal.setSelection(this.val$editTextFinal.length());
                    } else if (!obj.equals("" + intValue)) {
                        this.val$editTextFinal.setText("" + intValue);
                        this.val$editTextFinal.setSelection(this.val$editTextFinal.length());
                    }
                }
            } catch (Throwable e) {
              //  FileLog.m18e("tmessages", e);
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.13 */
    class AnonymousClass13 implements OnClickListener {
        final /* synthetic */ List val$userIds;

        AnonymousClass13(List list, EditText editText) {
            this.val$userIds = list;
            this.val$finalEditText = editText;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            OnlineContactsActivity.this.didSelectMultiResult(this.val$userIds, false, this.val$finalEditText != null ? this.val$finalEditText.getText().toString() : "0");
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.1 */
    class C15041 extends ActionBarMenuOnItemClick
    {
        C15041() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                OnlineContactsActivity.this.finishFragment();
            } else if (i == OnlineContactsActivity.add_button) {
                //OnlineContactsActivity.this.presentFragment(new NewContactActivity());
            } else if (i == OnlineContactsActivity.refresh) {
                OnlineContactsActivity.this.reloadOnlineList();
            }
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.2 */
    class C15052 extends ActionBarMenuItemSearchListener
    {
        C15052() {
        }

        public void onSearchCollapse() {
            OnlineContactsActivity.this.searchListViewAdapter.searchDialogs(null);
            OnlineContactsActivity.this.searching = false;
            OnlineContactsActivity.this.listView.setFastScrollEnabled(true);
            OnlineContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
            OnlineContactsActivity.this.emptyTextView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
        }

        public void onSearchExpand() {
            OnlineContactsActivity.this.searching = true;
        }

        public void onTextChanged(EditText editText) {
            if (OnlineContactsActivity.this.searchListViewAdapter != null) {
                String obj = editText.getText().toString();
                if (obj.length() != 0) {
                    OnlineContactsActivity.this.searchWas = true;
                    if (OnlineContactsActivity.this.listView != null) {
                        OnlineContactsActivity.this.listView.setAdapter(OnlineContactsActivity.this.searchListViewAdapter);
                        OnlineContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    }
                    if (OnlineContactsActivity.this.emptyTextView != null) {
                        OnlineContactsActivity.this.emptyTextView.setText(LocaleController.getString("NoResult", R.string.NoResult));
                    }
                }
                OnlineContactsActivity.this.searchListViewAdapter.searchDialogs(obj);
            }
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.3 */
    class C15063 implements OnTouchListener {
        C15063() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.4 */
    class C15084 implements OnItemClickListener {

        /* renamed from: org.telegram.ui.OnlineContactsActivity.4.1 */
        class C15071 implements OnClickListener {
            final /* synthetic */ String val$arg1;

          
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", this.val$arg1, null));
                   
                } catch (Throwable e) {
                    //FileLog.m18e("tmessages", e);
                }
            }
        }

        C15084() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            User   user;
            Bundle bundle;
            if (OnlineContactsActivity.this.searching && OnlineContactsActivity.this.searchWas) {
                user = (User) OnlineContactsActivity.this.searchListViewAdapter.getItem(i);
                if (user != null) {
                    if (OnlineContactsActivity.this.searchListViewAdapter.isGlobalSearch(i)) {
                    
                    }
                    if (OnlineContactsActivity.this.returnAsResult) {
                        if (OnlineContactsActivity.this.ignoreUsers == null || !OnlineContactsActivity.this.ignoreUsers.containsKey(Integer.valueOf(user.id))) {
                            OnlineContactsActivity.this.didSelectResult(user, true, null);
                            return;
                        }
                        return;
                    
                }
                return;
            }
            int sectionForPosition = OnlineContactsActivity.this.listViewAdapter.getSectionForPosition(i);
            int positionInSectionForPosition = OnlineContactsActivity.this.listViewAdapter.getPositionInSectionForPosition(i);
           
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.5 */
    class C15095 implements OnScrollListener {
        C15095() {
        }      

        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == OnlineContactsActivity.add_button && OnlineContactsActivity.this.searching && OnlineContactsActivity.this.searchWas) {
                AndroidUtilities.hideKeyboard(OnlineContactsActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.6 */
    class C15106 implements TextWatcher {
        final /* synthetic */ EditText val$editTextFinal;

        C15106(EditText editText) {
            this.val$editTextFinal = editText;
        }

        public void afterTextChanged(Editable editable) {
            try {
                String obj = editable.toString();
                if (obj.length() != 0) {
                    int intValue = Utilities.parseInt(obj).intValue();
                    if (intValue < 0) {
                        this.val$editTextFinal.setText("0");
                        this.val$editTextFinal.setSelection(this.val$editTextFinal.length());
                    }
                }
            } catch (Throwable e) {
                //FileLog.m18e("tmessages", e);
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.7 */
    class C15117 implements OnClickListener {
        final /* synthetic */ EditText val$finalEditText;
        final /* synthetic */ User     val$user;

        C15117(User user, EditText editText) {
            this.val$finalEditText = editText;
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            OnlineContactsActivity.this.didSelectResult(this.val$user, false, this.val$finalEditText != null ? this.val$finalEditText.getText().toString() : "0");
        }
    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.8 */
    class C15128 implements OnClickListener {
        C15128() {
        }

    }

    /* renamed from: org.telegram.ui.OnlineContactsActivity.9 */
    class C15139 implements GroupCreateActivityDelegate
    {
        C15139() {
        }

        public void didSelectUsers(ArrayList<Integer> arrayList) {
            OnlineContactsActivity.this.showDeleteContactsConfirmation(arrayList);
        }
    }

    public OnlineContactsActivity(Bundle bundle) {
        super(bundle);
        this.creatingChat = false;
        this.allowBots = true;
        this.needForwardCount = true;
        this.selectAlertString = null;
        this.allowUsernameSearch = true;
        this.selectedContacts = new HashMap();
    }

    private void didSelectMultiResult(List<Integer> list, boolean z, String str) {
        if (!z || this.selectAlertString == null) {
            if (this.multiSelectDelegate != null) {
                this.multiSelectDelegate.didSelectContacts(list, str);
                this.multiSelectDelegate = null;
            }
            finishFragment();
        } else if (getParentActivity() != null) {
            User         user;
            String       str2;
            Builder builder = new Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (list.size() == add_button) {
                user = MessagesController.getInstance().getUser((Integer) list.get(search_button));
                str2 = this.selectAlertString;
                objArr = new Object[add_button];
                objArr[search_button] = UserObject.getUserName(user);
                formatStringSimple = LocaleController.formatStringSimple(str2, objArr);
            } else {
                String str3 = this.selectAlertString;
                Object[] objArr2 = new Object[add_button];
                objArr2[search_button] = list.size() + " " + LocaleController.getString("User", R.string.User);
                formatStringSimple = LocaleController.formatStringSimple(str3, objArr2);
            }
            if (this.needForwardCount) {
                objArr = new Object[refresh];
                objArr[search_button] = formatStringSimple;
                objArr[add_button] = LocaleController.getString("AddToTheGroupForwardCount", R.string.AddToTheGroupForwardCount);
                str2 = String.format("%s\n\n%s", objArr);           
                formatStringSimple = str2;
                editText = view;
            } else {
                editText = null;
            }
            builder.setMessage(formatStringSimple);
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new AnonymousClass13(list, editText));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);           
        }
    }

    private void didSelectResult(User user, boolean z, String str) {
        if (this.multiSelectMode) {
            selectUser(user);
        } else if (!z || this.selectAlertString == null) {
            if (this.delegate != null) {
                this.delegate.didSelectContact(user, str);
                this.delegate = null;
            }
            finishFragment();
        } else if (getParentActivity() == null) {
        } else {
            if (user.bot && user.bot_nochats) {
                try {
                    Toast.makeText(getParentActivity(), LocaleController.getString("BotCantJoinGroups", R.string.BotCantJoinGroups), Toast.LENGTH_SHORT).show();
                    return;
                } catch (Throwable e) {
                 //   FileLog.m18e("tmessages", e);
                    return;
                }
            }
            EditText editText;        
            CharSequence formatStringSimple = LocaleController.formatStringSimple(str2, objArr);
            if (user.bot || !this.needForwardCount) {
                editText = null;
            } else {
                Object[] objArr2 = new Object[refresh];
                objArr2[search_button] = formatStringSimple;
                objArr2[add_button] = LocaleController.getString("AddToTheGroupForwardCount", R.string.AddToTheGroupForwardCount);
                String format = String.format("%s\n\n%s", objArr2);            
                builder.setView(editText2);
                EditText view = editText2;
                formatStringSimple = format;
                editText = view;
            }
            builder.setMessage(formatStringSimple);
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new C15117(user, editText));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
            if (editText != null) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) editText.getLayoutParams();           
                }
                editText.setSelection(editText.getText().length());
            }
        }
    }

    private void doDeleteMultipleContacts() {
        Bundle bundle = new Bundle();
        presentFragment(groupCreateActivity);
    }

    private void initTheme() {
    }

    private void initThemeListView() {

    }

    private void initThemeSearchItem(ActionBarMenuItem actionBarMenuItem) {
//        if (ThemeUtil.m2005b()) {
//            actionBarMenuItem.getSearchField().setTextColor(AdvanceTheme.bb);
//            Drawable drawable = getParentActivity().getResources().getDrawable(R.drawable.ic_close_white);
//            drawable.setColorFilter(AdvanceTheme.ba, Mode.MULTIPLY);
//            actionBarMenuItem.getClearButton().setImageDrawable(drawable);
//        }
    }

    private void reloadOnlineList() {
        if (this.onlyOnlines) {
            ContactsController.getInstance().initOnlineUsersSectionsDict();
            this.listViewAdapter = new OnlineContactsAdapter(ApplicationLoader.applicationContext, this.onlyUsers, false, this.ignoreUsers, this.chat_id != 0);
            this.listView.setAdapter(this.listViewAdapter);
        }
    }


    private void selectUser(User user) {
        if (this.selectedContacts.containsKey(Integer.valueOf(user.id))) {
            this.selectedContacts.remove(Integer.valueOf(user.id));
        } else {
            this.selectedContacts.put(Integer.valueOf(user.id), null);
        }
        if (this.listViewAdapter != null) {
            this.listViewAdapter.notifyDataSetChanged();
        }
        if (this.searchListViewAdapter != null) {
            this.searchListViewAdapter.notifyDataSetChanged();
        }
    }

    private void showDeleteContactsConfirmation(ArrayList<Integer> arrayList) {
        Builder builder = new Builder(getParentActivity());
       troller.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void showHelpDialog() {
       }
    }

    private void updateVisibleRows(int i) {
        if (this.listView != null) {
            int childCount = this.listView.getChildCount();
            for (int i2 = search_button; i2 < childCount; i2 += add_button) {
                View childAt = this.listView.getChildAt(i2);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(i);
                }
            }
        }
    }

    public View createView(Context context) {
        int i = add_button;
        this.actionBar.setAllowOverlayTitle(true);
        if (this.destroyAfterSelect) {
            if (this.returnAsResult) {
                this.actionBar.setTitle(LocaleController.getString("SelectContact", R.string.SelectContact));
            } else if (this.createSecretChat) {
                this.actionBar.setTitle(LocaleController.getString("NewSecretChat", R.string.NewSecretChat));
            } else {
                this.actionBar.setTitle(LocaleController.getString("NewMessageTitle", R.string.NewMessageTitle));
            }
        } else if (this.onlyOnlines) {
            this.actionBar.setTitle(LocaleController.getString("OnlineContacts", R.string.OnlineContacts));
        } else {
            this.actionBar.setTitle(LocaleController.getString("Contacts", R.string.Contacts));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C15041());
        ActionBarMenu     createMenu                      = this.actionBar.createMenu();
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem((int) search_button, (int) R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C15052());
        actionBarMenuItemSearchListener.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
        this.searchListViewAdapter = new OnlineContactsSearchAdapter(context, this.ignoreUsers, this.allowUsernameSearch, false, false, this.allowBots);
        if (this.multiSelectMode) {
            this.searchListViewAdapter.setUseUserCell(true);
            this.searchListViewAdapter.setCheckedMap(this.selectedContacts);
        }
        if (this.onlyOnlines) {
            ContactsController.getInstance().initOnlineUsersSectionsDict();
            this.listViewAdapter = new OnlineContactsAdapter(context, this.onlyUsers, false, this.ignoreUsers, this.chat_id != 0);
        }
        this.fragmentView = new FrameLayout(context);
        LinearLayout linearLayout = new LinearLayout(context);

        linearLayout.setVisibility(LinearLayout.INVISIBLE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ((FrameLayout) this.fragmentView).addView(linearLayout);
        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        this.emptyTextView.setTextColor(-8355712);
        
        this.emptyTextView.setTextSize(add_button, 20.0f);
        this.emptyTextView.setGravity(17);
        this.emptyTextView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
        linearLayout.addView(this.emptyTextView);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.emptyTextView.getLayoutParams();
        layoutParams2.width = -1;
        layoutParams2.height = -1;
        layoutParams2.weight = 0.5f;
        this.emptyTextView.setLayoutParams(layoutParams2);
        this.listView.setAdapter(this.listViewAdapter);
        this.listView.setFastScrollAlwaysVisible(true);
        LetterSectionsListView letterSectionsListView = this.listView;
        if (!LocaleController.isRTL) {
            i = refresh;
        }
        letterSectionsListView.setVerticalScrollbarPosition(i);
        ((FrameLayout) this.fragmentView).addView(this.listView);
        layoutParams = (LayoutParams) this.listView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        this.listView.setLayoutParams(layoutParams);
        this.listView.setOnItemClickListener(new C15084());
        this.listView.setOnScrollListener(new C15095());
        showHelpDialog();
        return this.fragmentView;
    }

    public void didReceivedNotification(int i, Object... objArr) {
       
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.contactsDidLoaded);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.closeChats);
        this.onlyOnlines = true;
        if (this.arguments != null) {
            this.onlyUsers = getArguments().getBoolean("onlyUsers", false);          
            this.allowBots = this.arguments.getBoolean("allowBots", true);
            this.chat_id = this.arguments.getInt("chat_id", search_button);
            this.multiSelectMode = this.arguments.getBoolean("multiSelectMode", false);
        } else {
            this.needPhonebook = true;
        }
        ContactsController.getInstance().checkInviteText();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.closeChats);
        this.delegate = null;
    }

    public void onPause() {
        super.onPause();
        if (this.actionBar != null) {
            this.actionBar.closeSearchField();
        }
    }

    public void onResume() {
        super.onResume();
        if (this.listViewAdapter != null) {
            this.listViewAdapter.notifyDataSetChanged();
        }
        initTheme();
    }

    public void setDelegate(ContactsActivityDelegate contactsActivityDelegate) {
        this.delegate = contactsActivityDelegate;
    }

    public void setIgnoreUsers(HashMap<Integer, User> hashMap) {
        this.ignoreUsers = hashMap;
    }

    public void setMultiSelectDelegate(ContactsActivityMultiSelectDelegate contactsActivityMultiSelectDelegate) {
        this.multiSelectDelegate = contactsActivityMultiSelectDelegate;
    }
}
