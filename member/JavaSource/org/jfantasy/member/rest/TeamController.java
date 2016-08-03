package org.jfantasy.member.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.framework.spring.validation.RESTful;
import org.jfantasy.member.bean.Invite;
import org.jfantasy.member.bean.Team;
import org.jfantasy.member.bean.TeamMember;
import org.jfantasy.member.rest.models.assembler.TeamResourceAssembler;
import org.jfantasy.member.service.InviteService;
import org.jfantasy.member.service.TeamMemberService;
import org.jfantasy.member.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "team", description = "团队")
@RestController
@RequestMapping("/teams")
public class TeamController {

    private TeamResourceAssembler assembler = new TeamResourceAssembler();

    @Autowired
    private TeamService teamService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private TeamMemberService teamMemberService;

    @ApiOperation(value = "团队列表", notes = "团队列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Team> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.teamService.findPager(pager, filters));
    }

    @ApiOperation(value = "查看团队", notes = "查看团队")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(this.get(id));
    }

    @ApiOperation(value = "添加团队", notes = "添加团队")
    @RequestMapping(method = RequestMethod.POST)
    public ResultResourceSupport create(@Validated(RESTful.POST.class) @RequestBody Team team) {
        return assembler.toResource(this.teamService.save(team));
    }

    @ApiOperation(value = "更新团队", notes = "更新团队地址")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResultResourceSupport update(@PathVariable("id") String id, @RequestBody Team team) {
        team.setKey(id);
        return assembler.toResource(this.teamService.update(team));
    }

    @ApiOperation(value = "删除团队", notes = "删除团队")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        this.teamService.deltele(id);
    }



    @ApiOperation(value = "邀请列表", notes = "邀请列表")
    @RequestMapping(value = "/{id}/invites", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> invites(@PathVariable("id") String id, Pager<Invite> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_team.key", id));
        return InviteController.assembler.toResources(this.inviteService.findPager(pager, filters));
    }

    @ApiOperation(value = "批量邀请", notes = "批量邀请")
    @RequestMapping(value = "/{id}/invites", method = RequestMethod.POST)
    @ResponseBody
    public List<ResultResourceSupport> invites(@PathVariable("id") String id, @RequestBody List<Invite> invites) {
        return InviteController.assembler.toResources(inviteService.save(id, invites));
    }

    @ApiOperation(value = "团队成员列表")
    @RequestMapping(value = "/{id}/member", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> members(@PathVariable("id") String id, Pager<TeamMember> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_team.key", id));
        return TeamMemberController.assembler.toResources(this.teamMemberService.findPager(pager, filters));
    }

    private Team get(String id) {
        Team team = this.teamService.get(id);
        if (team == null) {
            throw new NotFoundException("[id =" + id + "]对应的团队信息不存在");
        }
        return team;
    }

}
