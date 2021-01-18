insert into oauth_client_details(client_id, resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
values('test',null,'{noop}test','read,write','password,refresh_token',null,'ROLE_USER',36000,50000,null,null);

insert into user(id, email, password, role)
values(null,'pop8682@gmail.com','{bcrypt}$2a$10$476wqdnMSj0Rb5RgxXLLIOhzzPm2iT4HGmM7qE6JR9rxseyUgy.yK','ROLE_USER');