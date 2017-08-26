package org.telegram.Adel.OnlineContacts;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import org.telegram.Adel.Olds.BaseSectionsAdapter;
import org.telegram.Adel.Olds.GreySectionCell;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC.TL_contact;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.UserCell;

/* renamed from: org.telegram.mobo.m */
public class OnlineContactsAdapter extends BaseSectionsAdapter
{
    private Context                f1575a;
    private boolean                f1576b;
    private boolean                f1577c;
    private HashMap<Integer, User> f1578d;
    private HashMap<Integer, ?>    f1579e;
    private boolean                f1580f;
    private boolean                f1581g;

    public OnlineContactsAdapter(Context context, boolean z, boolean z2, HashMap<Integer, User> hashMap, boolean z3) {
        this.f1575a = context;
        this.f1576b = z;
        this.f1577c = z2;
        this.f1578d = hashMap;
        this.f1581g = z3;
    }

    private void m1431a(View view) {
//        if (ThemeUtil.m2005b()) {
//            int i = AdvanceTheme.aU;
//            i = AdvanceTheme.aV;
//            if (i > 0) {
//                view.setBackgroundColor(0);
//            } else {
//                view.setBackgroundColor(0);
//            }
//            if (i > 0) {
//                view.setTag("Contacts00");
//            }
//        }
    }

    private void m1432a(ViewGroup viewGroup) {
//        if (ThemeUtil.m2005b()) {
//            int i = AdvanceTheme.aU;
//            int i2 = AdvanceTheme.aV;
//            if (i2 > 0) {
//                Orientation orientation;
//                switch (i2) {
//                    case VideoPlayer.STATE_PREPARING /*2*/:
//                        orientation = Orientation.LEFT_RIGHT;
//                        break;
//                    case VideoPlayer.STATE_BUFFERING /*3*/:
//                        orientation = Orientation.TL_BR;
//                        break;
//                    case VideoPlayer.STATE_READY /*4*/:
//                        orientation = Orientation.BL_TR;
//                        break;
//                    default:
//                        orientation = Orientation.TOP_BOTTOM;
//                        break;
//                }
//                int i3 = AdvanceTheme.aW;
//                viewGroup.setBackgroundDrawable(new GradientDrawable(orientation, new int[]{i, i3}));
//                return;
//            }
//            viewGroup.setBackgroundColor(i);
//        }
    }

    public int getCountForSection(int i) {
        int size;
        if (!this.f1576b || this.f1581g) {
            if (i == 0) {
                return (this.f1577c || this.f1581g) ? 2 : 4;
            } else {
                if (i - 1 < ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
                    size = ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i - 1))).size();
                    return (i + -1 != ContactsController.getInstance().onlineSortedUsersSectionsArray.size() + -1 || this.f1577c) ? size + 1 : size;
                }
            }
        } else if (i < ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
            size = ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i))).size();
            return (i != ContactsController.getInstance().onlineSortedUsersSectionsArray.size() + -1 || this.f1577c) ? size + 1 : size;
        }
        return this.f1577c ? ContactsController.getInstance().phoneBookContacts.size() : 0;
    }

    public Object getItem(int i, int i2) {
        ArrayList arrayList;
        if (this.f1576b && !this.f1581g) {
            if (i < ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
                arrayList = (ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i));
                if (i2 < arrayList.size()) {
                    return MessagesController.getInstance().getUser(Integer.valueOf(((TL_contact) arrayList.get(i2)).user_id));
                }
            }
            return null;
        } else if (i == 0) {
            return null;
        } else {
            if (i - 1 >= ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
                return this.f1577c ? ContactsController.getInstance().phoneBookContacts.get(i2) : null;
            } else {
                arrayList = (ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i - 1));
                return i2 < arrayList.size() ? MessagesController.getInstance().getUser(Integer.valueOf(((TL_contact) arrayList.get(i2)).user_id)) : null;
            }
        }
    }

    public View getItemView(int i, int i2, View view, ViewGroup viewGroup) {
        View view2;
        float f = 72.0f;
        boolean z = true;
        int itemViewType = getItemViewType(i, i2);
//        int c = AdvanceTheme.m1799c(AdvanceTheme.aS, Theme.ACTION_BAR_ACTION_MODE_TEXT_COLOR);
//        int c2 = AdvanceTheme.m1799c(AdvanceTheme.aS, Theme.MSG_TEXT_COLOR);
//        int i3 = AdvanceTheme.aT;
        if (itemViewType == 4) {
            if (view == null) {
                view = new DividerCell(this.f1575a);
                itemViewType = AndroidUtilities.dp(LocaleController.isRTL ? 28.0f : 72.0f);
                if (!LocaleController.isRTL) {
                    f = 28.0f;
                }
                view.setPadding(itemViewType, 0, AndroidUtilities.dp(f), 0);
            }
            m1431a(view);
            view2 = view;
        } else if (itemViewType == 3) {
            if (view == null) {
                view2 = new GreySectionCell(this.f1575a);
                ((GreySectionCell) view2).setText(LocaleController.getString("Contacts", R.string.Contacts).toUpperCase());
//                if (ThemeUtil.m2005b()) {
//                    ((GreySectionCell) view2).setBackgroundColor(AdvanceTheme.aU);
//                    ((GreySectionCell) view2).setTextColor(c);
//                }
            } else {
                view2 = view;
            }
            m1431a(view2);
        } else if (itemViewType == 2) {
            view2 = view == null ? new TextCell(this.f1575a) : view;
            m1431a(view2);
            TextCell textCell = (TextCell) view2;
//            if (ThemeUtil.m2005b()) {
//                textCell.setTextColor(c2);
//            }
            if (this.f1577c) {
                textCell.setTextAndIcon(LocaleController.getString("InviteFriends", R.string.InviteFriends), R.drawable.menu_invite);
            } else if (this.f1581g) {
                textCell.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", R.string.InviteToGroupByLink), R.drawable.menu_invite);
            } else if (i2 == 0) {
                textCell.setTextAndIcon(LocaleController.getString("NewGroup", R.string.NewGroup), R.drawable.menu_newgroup);
            } else if (i2 == 1) {
                textCell.setTextAndIcon(LocaleController.getString("NewSecretChat", R.string.NewSecretChat), R.drawable.menu_secret);
            } else if (i2 == 2) {
                textCell.setTextAndIcon(LocaleController.getString("NewChannel", R.string.NewChannel), R.drawable.menu_broadcast);
            }
//            if (ThemeUtil.m2005b()) {
//                textCell.setIconColor(i3);
//            }
        } else if (itemViewType == 1) {
            View view3;
            if (view == null) {
                view2 = new TextCell(this.f1575a);
//                if (ThemeUtil.m2005b()) {
//                    ((TextCell) view2).setTextColor(c2);
//                    ((TextCell) view2).setTextSize(AdvanceTheme.aX);
//                }
                view3 = view2;
            } else {
                view3 = view;
            }
            m1431a(view3);
            Contact contact = (Contact) ContactsController.getInstance().phoneBookContacts.get(i2);
            if (contact.first_name != null && contact.last_name != null) {
                ((TextCell) view3).setText(contact.first_name + " " + contact.last_name);
            } else if (contact.first_name == null || contact.last_name != null) {
                ((TextCell) view3).setText(contact.last_name);
            } else {
                ((TextCell) view3).setText(contact.first_name);
            }
            view2 = view3;
        } else if (itemViewType == 0) {
            if (view == null) {
                view2 = new UserCell(this.f1575a, 58, 1, false);
                ((UserCell) view2).setStatusColors(-5723992, -12876608);
            } else {
                view2 = view;
            }
            m1431a(view2);
            HashMap hashMap = ContactsController.getInstance().onlineUsersSectionsDict;
            ArrayList arrayList = ContactsController.getInstance().onlineSortedUsersSectionsArray;
            itemViewType = (!this.f1576b || this.f1581g) ? 1 : 0;
            User user = MessagesController.getInstance().getUser(Integer.valueOf(((TL_contact) ((ArrayList) hashMap.get(arrayList.get(i - itemViewType))).get(i2)).user_id));
            ((UserCell) view2).setData(user, null, null, 0);
            if (this.f1579e != null) {
                UserCell userCell    = (UserCell) view2;
                boolean  containsKey = this.f1579e.containsKey(Integer.valueOf(user.id));
                if (this.f1580f || VERSION.SDK_INT <= 10) {
                    z = false;
                }
                userCell.setChecked(containsKey, z);
            }
            if (this.f1578d != null) {
                if (this.f1578d.containsKey(Integer.valueOf(user.id))) {
                    view2.setAlpha(0.5f);
                } else {
                    view2.setAlpha(1f);
                }
            }
        } else {
            view2 = view;
        }
        m1432a(viewGroup);
        return view2;
    }

    public int getItemViewType(int i, int i2) {
        int i3 = 0;
        if (this.f1576b && !this.f1581g) {
            return i2 < ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i))).size() ? 0 : 4;
        } else {
            if (i == 0) {
                if (this.f1577c || this.f1581g) {
                    if (i2 == 1) {
                        return 3;
                    }
                } else if (i2 == 3) {
                    return 3;
                }
                return 2;
            } else if (i - 1 >= ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
                return 1;
            } else {
                if (i2 >= ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i - 1))).size()) {
                    i3 = 4;
                }
                return i3;
            }
        }
    }

    public int getSectionCount() {
        int size = ContactsController.getInstance().onlineSortedUsersSectionsArray.size();
        if (!this.f1576b) {
            size++;
        }
        if (this.f1581g) {
            size++;
        }
        return this.f1577c ? size + 1 : size;
    }

    public View getSectionHeaderView(int i, View view, ViewGroup viewGroup) {
        View letterSectionCell = view == null ? new LetterSectionCell(this.f1575a) : view;
        if (!this.f1576b || this.f1581g) {
            if (i == 0) {
                ((LetterSectionCell) letterSectionCell).setLetter("");
            } else if (i - 1 < ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
                ((LetterSectionCell) letterSectionCell).setLetter((String) ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i - 1));
            } else {
                ((LetterSectionCell) letterSectionCell).setLetter("");
            }
        } else if (i < ContactsController.getInstance().onlineSortedUsersSectionsArray.size()) {
            ((LetterSectionCell) letterSectionCell).setLetter((String) ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i));
        } else {
            ((LetterSectionCell) letterSectionCell).setLetter("");
        }
        return letterSectionCell;
    }

    public int getViewTypeCount() {
        return 5;
    }

    public boolean isRowEnabled(int i, int i2) {
        if (!this.f1576b || this.f1581g) {
            return i == 0 ? (this.f1577c || this.f1581g) ? i2 != 1 : i2 != 3 : i + -1 >= ContactsController.getInstance().onlineSortedUsersSectionsArray.size() || i2 < ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i - 1))).size();
        } else {
            return i2 < ((ArrayList) ContactsController.getInstance().onlineUsersSectionsDict.get(ContactsController.getInstance().onlineSortedUsersSectionsArray.get(i))).size();
        }
    }
}
