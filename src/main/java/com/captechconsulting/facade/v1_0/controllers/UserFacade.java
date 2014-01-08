package com.captechconsulting.facade.v1_0.controllers;

import com.captechconsulting.core.domain.User;
import com.captechconsulting.facade.Versions;
import com.captechconsulting.core.service.UserService;
import com.captechconsulting.facade.v1_0.data.UserVO;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Transactional
@Controller
@RequestMapping("/user")
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private DozerBeanMapper mapper;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = Versions.V1_0, consumes = Versions.V1_0)
    public @ResponseBody UserVO getUser(@PathVariable long userId) {
        User user = userService.getUser(userId);
        return mapper.map(user, UserVO.class);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, produces = Versions.V1_0, consumes = Versions.V1_0)
    public @ResponseBody UserVO update(@PathVariable long userId, @Valid @RequestBody UserVO user) {
        User previouslyPersisted = userService.getUser(userId);
        mapper.map(user, previouslyPersisted);
        User persisted = userService.store(previouslyPersisted);
        return mapper.map(persisted, UserVO.class);
    }

    @RequestMapping(method = RequestMethod.POST, produces = Versions.V1_0, consumes = Versions.V1_0)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody UserVO create(@Valid @RequestBody UserVO user) {
        User mappedUser = mapper.map(user, User.class);
        User persisted = userService.store(mappedUser);
        return mapper.map(persisted, UserVO.class);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, produces = Versions.V1_0, consumes = Versions.V1_0)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody UserVO remove(@PathVariable long userId) {
        User user = userService.getUser(userId);
        userService.delete(user);
        return null;
    }

}
