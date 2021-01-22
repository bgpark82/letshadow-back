insert into oauth_client_details(client_id, resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
values('test',null,'{noop}test','read,write','password,refresh_token',null,'ROLE_USER',36000,50000,null,null);
