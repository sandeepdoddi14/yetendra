package com.darwinbox.attendance.objects;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase.getFilter;
import static com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase.parseToPHP;

public class EmployeeProfileEmailSettings {

    private boolean manager_weekly_report;
    private boolean public_update;
    private boolean public_question;
    private boolean public_poll;
    private boolean public_event;
    private boolean group_update;
    private boolean group_question;
    private boolean group_poll;
    private boolean group_event;
    private boolean leave_request;
    private boolean leave_response;
    private boolean request_owner;
    private boolean request_user;
    private boolean request_attendance;
    private boolean response_attendance;
    private boolean request_compoff;
    private boolean response_compoff;
    private boolean request_optionalholiday;
    private boolean response_optionalholiday;
    private boolean request_checkinrequests;
    private boolean other_like_message;
    private boolean other_reply_message;
    private boolean other_mention;
    private boolean other_private_message;
    private boolean request_expense;
    private boolean reset_password;
    private boolean assign_shift;
    private boolean clocking_warning;
    private boolean absent_warning;

    public boolean isManager_weekly_report() {
        return manager_weekly_report;
    }

    public void setManager_weekly_report(boolean manager_weekly_report) {
        this.manager_weekly_report = manager_weekly_report;
    }

    public boolean isPublic_update() {
        return public_update;
    }

    public void setPublic_update(boolean public_update) {
        this.public_update = public_update;
    }

    public boolean isPublic_question() {
        return public_question;
    }

    public void setPublic_question(boolean public_question) {
        this.public_question = public_question;
    }

    public boolean isPublic_poll() {
        return public_poll;
    }

    public void setPublic_poll(boolean public_poll) {
        this.public_poll = public_poll;
    }

    public boolean isPublic_event() {
        return public_event;
    }

    public void setPublic_event(boolean public_event) {
        this.public_event = public_event;
    }

    public boolean isGroup_update() {
        return group_update;
    }

    public void setGroup_update(boolean group_update) {
        this.group_update = group_update;
    }

    public boolean isGroup_question() {
        return group_question;
    }

    public void setGroup_question(boolean group_question) {
        this.group_question = group_question;
    }

    public boolean isGroup_poll() {
        return group_poll;
    }

    public void setGroup_poll(boolean group_poll) {
        this.group_poll = group_poll;
    }

    public boolean isGroup_event() {
        return group_event;
    }

    public void setGroup_event(boolean group_event) {
        this.group_event = group_event;
    }

    public boolean isLeave_request() {
        return leave_request;
    }

    public void setLeave_request(boolean leave_request) {
        this.leave_request = leave_request;
    }

    public boolean isLeave_response() {
        return leave_response;
    }

    public void setLeave_response(boolean leave_response) {
        this.leave_response = leave_response;
    }

    public boolean isRequest_owner() {
        return request_owner;
    }

    public void setRequest_owner(boolean request_owner) {
        this.request_owner = request_owner;
    }

    public boolean isRequest_user() {
        return request_user;
    }

    public void setRequest_user(boolean request_user) {
        this.request_user = request_user;
    }

    public boolean isRequest_attendance() {
        return request_attendance;
    }

    public void setRequest_attendance(boolean request_attendance) {
        this.request_attendance = request_attendance;
    }

    public boolean isResponse_attendance() {
        return response_attendance;
    }

    public void setResponse_attendance(boolean response_attendance) {
        this.response_attendance = response_attendance;
    }

    public boolean isRequest_compoff() {
        return request_compoff;
    }

    public void setRequest_compoff(boolean request_compoff) {
        this.request_compoff = request_compoff;
    }

    public boolean isResponse_compoff() {
        return response_compoff;
    }

    public void setResponse_compoff(boolean response_compoff) {
        this.response_compoff = response_compoff;
    }

    public boolean isRequest_optionalholiday() {
        return request_optionalholiday;
    }

    public void setRequest_optionalholiday(boolean request_optionalholiday) {
        this.request_optionalholiday = request_optionalholiday;
    }

    public boolean isResponse_optionalholiday() {
        return response_optionalholiday;
    }

    public void setResponse_optionalholiday(boolean response_optionalholiday) {
        this.response_optionalholiday = response_optionalholiday;
    }

    public boolean isRequest_checkinrequests() {
        return request_checkinrequests;
    }

    public void setRequest_checkinrequests(boolean request_checkinrequests) {
        this.request_checkinrequests = request_checkinrequests;
    }

    public boolean isOther_like_message() {
        return other_like_message;
    }

    public void setOther_like_message(boolean other_like_message) {
        this.other_like_message = other_like_message;
    }

    public boolean isOther_reply_message() {
        return other_reply_message;
    }

    public void setOther_reply_message(boolean other_reply_message) {
        this.other_reply_message = other_reply_message;
    }

    public boolean isOther_mention() {
        return other_mention;
    }

    public void setOther_mention(boolean other_mention) {
        this.other_mention = other_mention;
    }

    public boolean isOther_private_message() {
        return other_private_message;
    }

    public void setOther_private_message(boolean other_private_message) {
        this.other_private_message = other_private_message;
    }

    public boolean isRequest_expense() {
        return request_expense;
    }

    public void setRequest_expense(boolean request_expense) {
        this.request_expense = request_expense;
    }

    public boolean isReset_password() {
        return reset_password;
    }

    public void setReset_password(boolean reset_password) {
        this.reset_password = reset_password;
    }

    public boolean isAssign_shift() {
        return assign_shift;
    }

    public void setAssign_shift(boolean assign_shift) {
        this.assign_shift = assign_shift;
    }

    public boolean isClocking_warning() {
        return clocking_warning;
    }

    public void setClocking_warning(boolean clocking_warning) {
        this.clocking_warning = clocking_warning;
    }

    public boolean isAbsent_warning() {
        return absent_warning;
    }

    public void setAbsent_warning(boolean absent_warning) {
        this.absent_warning = absent_warning;
    }

    public void toObject(Map<String,String> body) {
        setClocking_warning(getFilter(body, "ClockIn","yes" ));
    }

    public List<NameValuePair> toListObject() {

        List<NameValuePair> body = new ArrayList<>();

        body.add(new BasicNameValuePair("yt0","Update"));
        body.add(new BasicNameValuePair("UserSettings[notify][public_update]",parseToPHP(isPublic_update())));
        body.add(new BasicNameValuePair("UserSettings[manager_weekly_report]",parseToPHP(isManager_weekly_report())));
        body.add(new BasicNameValuePair("UserSettings[notify][public_poll]",parseToPHP(isPublic_poll())));
        body.add(new BasicNameValuePair("UserSettings[notify][public_event]",parseToPHP(isPublic_event())));
        body.add(new BasicNameValuePair("UserSettings[notify][public_question]",parseToPHP(isPublic_question())));
        body.add(new BasicNameValuePair("UserSettings[notify][group_update]",parseToPHP(isGroup_update())));
        body.add(new BasicNameValuePair("UserSettings[notify][group_question]",parseToPHP(isGroup_question())));
        body.add(new BasicNameValuePair("UserSettings[notify][group_poll]",parseToPHP(isGroup_poll())));
        body.add(new BasicNameValuePair("UserSettings[notify][group_event]",parseToPHP(isGroup_event())));
        body.add(new BasicNameValuePair("UserSettings[notify][leave_request]",parseToPHP(isLeave_request())));
        body.add(new BasicNameValuePair("UserSettings[notify][leave_response]",parseToPHP(isLeave_response())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_owner]",parseToPHP(isRequest_owner())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_user]",parseToPHP(isRequest_user())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_attendance]",parseToPHP(isRequest_attendance())));
        body.add(new BasicNameValuePair("UserSettings[notify][response_attendance]",parseToPHP(isResponse_attendance())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_compoff]",parseToPHP(isRequest_compoff())));
        body.add(new BasicNameValuePair("UserSettings[notify][response_compoff]",parseToPHP(isResponse_compoff())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_optionalholiday]",parseToPHP(isRequest_optionalholiday())));
        body.add(new BasicNameValuePair("UserSettings[notify][response_optionalholiday]",parseToPHP(isResponse_optionalholiday())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_checkinrequests]",parseToPHP(isRequest_checkinrequests())));
        body.add(new BasicNameValuePair("UserSettings[notify][other_like_message]",parseToPHP(isOther_like_message())));
        body.add(new BasicNameValuePair("UserSettings[notify][other_reply_message]",parseToPHP(isOther_reply_message())));
        body.add(new BasicNameValuePair("UserSettings[notify][other_mention]",parseToPHP(isOther_mention())));
        body.add(new BasicNameValuePair("UserSettings[notify][other_private_message]",parseToPHP(isOther_private_message())));
        body.add(new BasicNameValuePair("UserSettings[notify][request_expense]",parseToPHP(isRequest_expense())));
        body.add(new BasicNameValuePair("UserSettings[notify][reset_password]",parseToPHP(isReset_password())));
        body.add(new BasicNameValuePair("UserSettings[notify][assign_shift]",parseToPHP(isAssign_shift())));
        body.add(new BasicNameValuePair("UserShiftForm[clocking_warning]",parseToPHP(isClocking_warning())));
        body.add(new BasicNameValuePair("UserShiftForm[absent_warning]",parseToPHP(isAbsent_warning())));

        return body;
    }
}
