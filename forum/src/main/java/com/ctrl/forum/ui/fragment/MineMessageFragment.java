package com.ctrl.forum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.ctrl.forum.R;
import com.ctrl.forum.base.Constant;
import com.ctrl.forum.dao.ReplyCommentDao;
import com.ctrl.forum.entity.Message;
import com.ctrl.forum.ui.activity.Invitation.InvitationPinterestDetailActivity;
import com.ctrl.forum.ui.activity.mine.MineOrderActivity;
import com.ctrl.forum.ui.activity.mine.MineXianJuanActivity;
import com.ctrl.forum.ui.activity.mine.MineYouJuanActivity;
import com.ctrl.forum.ui.adapter.MineMessageListAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息-全部
 */
public class MineMessageFragment extends ToolBarFragment {
    private List<Message> messages;
    private PullToRefreshListView lv_content;
    private MineMessageListAdapter mineMessageListAdapter;
    private int PAGE_NUM=1;
    private ReplyCommentDao rdao;
    private List<Message> messageList = new ArrayList<>();

    public static MineMessageFragment newInstance() {
        MineMessageFragment fragment = new MineMessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_list, container, false);
        lv_content = (PullToRefreshListView) view.findViewById(R.id.lv_content);
        mineMessageListAdapter = new MineMessageListAdapter(getActivity());
        lv_content.setAdapter(mineMessageListAdapter);

        initData();

        lv_content.setMode(PullToRefreshBase.Mode.BOTH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (messages != null) {
                    messages.clear();
                    PAGE_NUM = 1;
                    mineMessageListAdapter = new MineMessageListAdapter(getActivity());
                    lv_content.setAdapter(mineMessageListAdapter);
                }
                rdao.queryMessageList(Arad.preferences.getString("memberId"), PAGE_NUM, Constant.PAGE_SIZE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (messages != null) {
                    PAGE_NUM += 1;
                    rdao.queryMessageList(Arad.preferences.getString("memberId"), PAGE_NUM, Constant.PAGE_SIZE);
                } else {
                    lv_content.onRefreshComplete();
                }
            }
        });

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (messageList != null && messageList.get(position - 1) != null) {
                    rdao.modifyReadState(messageList.get(position - 1).getId());//更改消息通知为已读
                    String state = messageList.get(position - 1).getMsgType();
                    String targetId = messageList.get(position - 1).getTargetId();
                    Intent intent;
                    switch (state) {
                        case "1": //用户下单支付成功<通知商家>
                            intent = new Intent(getActivity(), MineOrderActivity.class);
                            startActivity(intent);
                            break;
                        case "2": //商家发货<通知买家>
                            intent = new Intent(getActivity(), MineOrderActivity.class);
                            startActivity(intent);
                            break;
                        case "3": //买家领取优惠券<通知买家>
                            intent = new Intent(getActivity(), MineYouJuanActivity.class);
                            startActivity(intent);
                            break;
                        case "4": //商家赠送现金券给买家<通知买家>
                            intent = new Intent(getActivity(), MineXianJuanActivity.class);
                            startActivity(intent);
                            break;
                        case "5": //会员发布帖子<通知会员>
                            intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                            intent.putExtra("id", targetId);
                            startActivity(intent);
                            break;
                        case "6": //已发布帖子需要审核<通知会员>
                            intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                            intent.putExtra("id", targetId);
                            startActivity(intent);
                            break;
                        case "7": //帖子被赞<通知发帖人>
                            intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                            intent.putExtra("id", targetId);
                            startActivity(intent);
                            break;
                        case "8": //帖子收到评论
                            intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                            intent.putExtra("id", targetId);
                            startActivity(intent);
                            break;
                        case "9": //帖子评论收到回复
                            intent = new Intent(getActivity(), InvitationPinterestDetailActivity.class);
                            intent.putExtra("id", targetId);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        return view;

    }

    private void initData() {
        rdao = new ReplyCommentDao(this);
        rdao.queryMessageList(Arad.preferences.getString("memberId"), PAGE_NUM, Constant.PAGE_SIZE);
    }

    @Override
    public void onRequestSuccess(int requestCode) {
        super.onRequestSuccess(requestCode);
        lv_content.onRefreshComplete();
        if (requestCode==4){
            messages = rdao.getMessages();
            if (messages!=null){
                mineMessageListAdapter.setMessages(messages);
                this.messageList.addAll(messages);
            }

        }
    }

    @Override
    public void onRequestFaild(String errorNo, String errorMessage) {
        super.onRequestFaild(errorNo, errorMessage);
        lv_content.onRefreshComplete();
    }

}
