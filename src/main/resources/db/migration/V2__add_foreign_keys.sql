alter table moons
    add constraint FK1bllnyryaoi7flc5keoa7iqyj foreign key (planet_id) references planets;
alter table planets
    add constraint FKl1i5l50704o2j1bu62xw1qo34 foreign key (star_id) references stars;